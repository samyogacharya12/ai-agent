package com.agent.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class RagService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagService(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    public String ask(String question) {
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore)
            .searchRequest(SearchRequest.builder()
                .topK(4)
                .similarityThreshold(0.6)
                .build())
            .build();

        return chatClient.prompt()
            .system("""
                        You are a document question-answering assistant.

                        Rules:
                        - Answer only from the retrieved document context.
                        - If the answer is not in the context, say you could not find it in the document.
                        - Keep the answer clear and short.
                        """)
            .user(question)
            .advisors(advisor)
            .call()
            .content();
    }
}
