package com.example.hashtag.controller;

import com.example.hashtag.consumer.KafkaHashtagConsumer;
import com.example.hashtag.model.HashtagStat;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final KafkaHashtagConsumer consumer;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/top-hashtags")
    public List<HashtagStat> getTopHashtags(@RequestParam(defaultValue = "10") int limit) {
        return consumer.getStats().entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> -entry.getValue()))
                .limit(limit)
                .map(entry -> new HashtagStat(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping("/hashtag/{tag}")
    public HashtagStat getHashtagStats(@PathVariable String tag) {
        return new HashtagStat(tag, consumer.getCountForHashtag(tag));
    }

    @GetMapping("/v2/top-hashtags")
    public List<HashtagStat> getTopHashtagsFromRedis() {
        Set<String> keys = redisTemplate.keys("hashtag:*");
        if (keys == null || keys.isEmpty())
            return List.of();

        return keys.stream()
                .map(key -> {
                    String tag = key.substring("hashtag:".length());
                    String countStr = redisTemplate.opsForValue().get(key);
                    int count = countStr != null ? Integer.parseInt(countStr) : 0;
                    return new HashtagStat(tag, count);
                })
                .sorted(Comparator.comparingInt(HashtagStat::getCount).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

}