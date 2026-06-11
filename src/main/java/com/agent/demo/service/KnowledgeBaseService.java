package com.agent.demo.service;

import com.agent.demo.enumconstant.ToolType;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KnowledgeBaseService {

    private final VectorStore vectorStore;


    public KnowledgeBaseService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }



    public String searchKnowledgeBase(String question, String conversationId) {
        return retrieveRelevantChunks(question, conversationId);
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

    public String retrieveRelevantChunks(String question,String conversationId) {
        SearchRequest request = SearchRequest.builder()
                .query(question)
                .topK(3)
                .similarityThreshold(0.3)
                .filterExpression(
                        "conversationId == '"
                                + conversationId
                                + "'"
                )
                .build();

        List<Document> documents =
                vectorStore.similaritySearch(request);

        System.out.println("Question: " + question);
        System.out.println("Documents found: " + documents.size());

        if (documents.isEmpty()) {
            return "No matching knowledge found.";
        }

        return documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n"));
    }
}
