package com.example.hashtag.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class KafkaHashtagConsumer {

    private final ConcurrentHashMap<String, Integer> hashtagCounts = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "captions", groupId = "hashtag-analytics-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            String value = record.value();
            JsonNode json = objectMapper.readTree(value);
            String hashtag = json.get("hashtag").asText();

            hashtagCounts.merge(hashtag, 1, Integer::sum);
            log.info("Processed hashtag: #{} → Count: {}", hashtag, hashtagCounts.get(hashtag));

        } catch (Exception e) {
            log.error("Error processing message: {}", record.value(), e);
        }
    }

    public ConcurrentHashMap<String, Integer> getStats() {
        return hashtagCounts;
    }

    public int getCountForHashtag(String tag) {
        return hashtagCounts.getOrDefault(tag.toLowerCase(), 0);
    }
}
