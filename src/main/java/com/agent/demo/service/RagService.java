package com.agent.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final MongoChatHistoryService mongoChatHistoryService;

    public RagService(ChatClient chatClient,
                      VectorStore vectorStore,
                      MongoChatHistoryService mongoChatHistoryService) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
        this.mongoChatHistoryService = mongoChatHistoryService;
    }

    public String ask(String conversationId, String question) {

        mongoChatHistoryService.saveUserMessage(conversationId, question);

        String historyText = mongoChatHistoryService.getRecentHistoryAsText(conversationId, 6);

        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore)
            .searchRequest(SearchRequest.builder()
                .topK(6)
                .similarityThreshold(0.65)
                .build())
            .build();

        String answer = chatClient.prompt()
            .system("""
                You are a document question-answering assistant.

                Rules:
                - Answer ONLY from retrieved document context.
                - If the answer is not in the context, say:
                  "I could not find this information in the document."
                - Do NOT use outside knowledge.
                - Do NOT guess.
                """)
            .user("""
                Recent conversation:
                %s

                Current question:
                %s
                """.formatted(historyText, question))
            .advisors(advisor)
            .call()
            .content();

        mongoChatHistoryService.saveAssistantMessage(conversationId, answer);

        return answer;
    }

    public RagDebugResponse askWithDebug(String conversationId, String question) {

        mongoChatHistoryService.saveUserMessage(conversationId, question);

        SearchRequest searchRequest = SearchRequest.builder()
            .query(question)
            .topK(6)
            .similarityThreshold(0.65)
            .build();

        List<Document> retrievedDocs = vectorStore.similaritySearch(searchRequest);

        if (retrievedDocs == null || retrievedDocs.isEmpty()) {
            String fallback = "I could not find this information in the document.";
            mongoChatHistoryService.saveAssistantMessage(conversationId, fallback);
            return new RagDebugResponse(fallback, List.of());
        }

        String historyText = mongoChatHistoryService.getRecentHistoryAsText(conversationId, 6);

        String retrievedContext = retrievedDocs.stream()
            .map(Document::getText)
            .collect(Collectors.joining("\n\n---\n\n"));

        String answer = chatClient.prompt()
            .system("""
                You are a document question-answering assistant.

                Rules:
                - Answer ONLY using the retrieved document context.
                - If the answer is not supported by the retrieved context, say:
                  I could not find this information in the document.
                - Do NOT use outside knowledge.
                - Do NOT guess.
                """)
            .user("""
                Recent conversation:
                %s

                Retrieved context:
                %s

                Current question:
                %s
                """.formatted(historyText, retrievedContext, question))
            .call()
            .content();

        mongoChatHistoryService.saveAssistantMessage(conversationId, answer);

        List<String> chunks = retrievedDocs.stream()
            .map(Document::getText)
            .toList();

        return new RagDebugResponse(answer, chunks);
    }

    public record RagDebugResponse(String answer, List<String> retrievedChunks) {
    }
}
