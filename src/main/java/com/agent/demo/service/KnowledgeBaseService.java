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
