package com.agent.demo.service;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class CoordinatorAgent {

    private final ChatClient chatClient;

    public CoordinatorAgent(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }


    public String run(String conversationId, String question) {

        return chatClient.prompt()
                .system("""
                        You are a coordinator agent.

                        Your job:
                        1. Understand the user's question.
                        2. If the question needs document knowledge, use the available MCP tool search_knowledge_base.
                        3. Use the returned context to answer clearly.
                        4. Do not make up information if the tool does not return relevant context.
                        """)
                .user("""
                        Conversation ID: %s

                        User question: %s
                        """.formatted(conversationId, question))
                .call()
                .content();
    }
}
