package com.example.hashtag.controller;

import com.example.hashtag.consumer.KafkaHashtagConsumer;
import com.example.hashtag.model.HashtagStat;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final KafkaHashtagConsumer consumer;

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
}