package com.example.caption.controller;

import com.example.caption.model.CaptionRequest;
import com.example.caption.producer.KafkaCaptionProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/captions")
@RequiredArgsConstructor
public class CaptionController {

    private final KafkaCaptionProducer producer;

    @PostMapping
    public ResponseEntity<String> postCaption(@RequestBody CaptionRequest request) {
        producer.sendCaption(request.getCaption());
        return ResponseEntity.ok("Caption received and hashtags sent to Kafka.");
    }
}