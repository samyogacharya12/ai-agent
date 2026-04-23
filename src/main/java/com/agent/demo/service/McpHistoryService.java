package com.agent.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class McpHistoryService {

    private final RestTemplate restTemplate;

    public McpHistoryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getConversationHistory(String conversationId) {
        return restTemplate.getForObject(
                "http://localhost:8081/api/history/" + conversationId,
                String.class
        );
    }
}
