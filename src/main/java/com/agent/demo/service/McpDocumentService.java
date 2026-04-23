package com.agent.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class McpDocumentService {

    private final RestTemplate restTemplate;

    public McpDocumentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String searchDocument(String conversationId, String question) {

        String body = """
        {
            "conversationId": "%s",
            "question": "%s"
        }
        """.formatted(conversationId, question);

        return restTemplate.postForObject(
                "http://localhost:8081/api/rag/ask",
                body,
                String.class
        );
    }

}
