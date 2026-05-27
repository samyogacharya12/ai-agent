package com.agent.demo.service;

import com.agent.demo.dto.ParamDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class McpClient {

    private final RestClient restClient;

    public McpClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:8081")
                .build();
    }

    public String searchHistory(
            String conversationId,
            String question
    ) {

        ParamDto request = new ParamDto();
        request.setConversationId(conversationId);
        request.setQuestion(question);

        return restClient.post()
                .uri("/api/history/search")
                .body(request)
                .retrieve()
                .body(String.class);
    }


    public String askRag(String conversationId, String question) {

        RagRequest request = new RagRequest(
                conversationId,
                question
        );

        return restClient.post()
                .uri("/api/rag/ask")
                .body(request)
                .retrieve()
                .body(String.class);
    }

    public String searchDocuments(String question) {
        Map<String, String> request = Map.of("question", question);

        return restClient.post()
                .uri("/mcp/rag/search")
                .body(request)
                .retrieve()
                .body(String.class);
    }

    public String getConversationHistory(String conversationId) {
        return restClient.get()
                .uri("/mcp/history/{conversationId}", conversationId)
                .retrieve()
                .body(String.class);
    }

    public record RagRequest(
            String conversationId,
            String question
    ) {}
}
