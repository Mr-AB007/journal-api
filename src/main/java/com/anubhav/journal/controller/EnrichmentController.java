package com.anubhav.journal.controller;

import com.anubhav.journal.service.HuggingFaceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/enrich")
public class EnrichmentController {
    private final HuggingFaceClient hfservice;

    public EnrichmentController(HuggingFaceClient hfservice) {
        this.hfservice = hfservice;
    }

    @PostMapping("/sentiment")
    public ResponseEntity<?> sentiment(@RequestBody Map<String,String> body) {
        String text = body.get("text");
        if (text == null || text.isBlank()) return ResponseEntity.badRequest().body("text missing");
        HuggingFaceClient.SentimentResult[] res = hfservice.sentiment(text);
        return ResponseEntity.ok(res);
    }
}
