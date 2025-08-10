package com.anubhav.journal.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class HuggingFaceClient {

    private final RestTemplate rest = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${huggingface.token}")
    private String hfToken;

    @Value("${huggingface.model}")
    private String hfModel; // e.g. "distilbert/distilbert-base-uncased-finetuned-sst-2-english"

    private static final String HF_API = "https://api-inference.huggingface.co/models/";

    public SentimentResult[] sentiment(String text) {
        try {
            String url = HF_API + hfModel;
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(hfToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON)); // safer Accept header

            Map<String, String> body = Map.of("inputs", text);
            HttpEntity<Map<String, String>> req = new HttpEntity<>(body, headers);

            ResponseEntity<RawSentimentResult[][]> resp =
                    rest.exchange(url, HttpMethod.POST, req, RawSentimentResult[][].class);

            RawSentimentResult[] rawResults = resp.getBody()[0]; // HuggingFace wraps results in an array

            SentimentResult[] readableResults = new SentimentResult[rawResults.length];
            for (int i = 0; i < rawResults.length; i++) {
                readableResults[i] = new SentimentResult(
                        rawResults[i].label,
                        String.format("%.2f%%", rawResults[i].score * 100),
                        getStrength(rawResults[i].score)
                );
            }

            return readableResults;

        } catch (Exception e) {
            throw new RuntimeException("Error calling HuggingFace API: " + e.getMessage(), e);
        }
    }

    // Qualitative strength mapping
    private String getStrength(double score) {
        if (score >= 0.75) return "Very Strong";
        else if (score >= 0.5) return "Strong";
        else if (score >= 0.25) return "Moderate";
        else return "Weak";
    }

    // Class for API response mapping
    public static class RawSentimentResult {
        public String label;
        public double score;
    }

    // simple DTO to map HF response objects like {"label":"POSITIVE","score":0.98}
    public static class SentimentResult {
        public String label;
        public String score; // percentage string
        public String strength;

        public SentimentResult(String label, String score, String strength) {
            this.label = label;
            this.score = score;
            this.strength = strength;
        }
    }
}
