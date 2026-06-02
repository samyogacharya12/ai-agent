package com.agent.demo.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class KnowledgeBaseService {


    private final VectorStore vectorStore;

    public KnowledgeBaseService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public String search(String question) {

        // For now this is simple demo knowledge.
        // Later you can replace this with vector DB / document search.

        if (question == null || question.isBlank()) {
            return "No question provided.";
        }

        String lowerQuestion = question.toLowerCase();

        if (lowerQuestion.contains("kafka")) {
            return """
                    Kafka is a distributed event streaming platform.
                    It is commonly used for real-time messaging, event-driven systems,
                    log processing, and microservice communication.
                    """;
        }

        if (lowerQuestion.contains("spring boot")) {
            return """
                    Spring Boot is a Java framework used to build backend applications,
                    REST APIs, and microservices with minimal configuration.
                    """;
        }

        if (lowerQuestion.contains("mcp")) {
            return """
                    MCP stands for Model Context Protocol.
                    It allows AI agents to connect with external tools and data sources
                    in a structured way.
                    """;
        }

        return """
                I could not find matching information in the knowledge base.
                Try asking about Kafka, Spring Boot, or MCP.
                """;
    }

    public String retrieveRelevantChunks(String question) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(question)
                .topK(5)
                .build();

        return vectorStore.similaritySearch(searchRequest)
                .stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));
    }


}
