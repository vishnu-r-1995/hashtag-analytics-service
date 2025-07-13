package com.example.caption.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class KafkaCaptionProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topicName = "captions";

    public void sendCaption(String caption) {
        List<String> hashtags = extractHashtags(caption);
        String timestamp = Instant.now().toString();

        for (String tag : hashtags) {
            String message = String.format("{\"caption\": \"%s\", \"hashtag\": \"%s\", \"timestamp\": \"%s\"}",
                    caption.replace("\"", "\\\""), tag, timestamp);
            kafkaTemplate.send(topicName, tag, message);
        }
    }

    private List<String> extractHashtags(String text) {
        Matcher matcher = Pattern.compile("#(\\w+)").matcher(text);
        return matcher.results()
                      .map(m -> m.group(1).toLowerCase())
                      .distinct()
                      .toList();
    }
}