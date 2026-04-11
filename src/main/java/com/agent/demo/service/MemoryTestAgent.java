package com.agent.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class MemoryTestAgent {


    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public MemoryTestAgent(ChatClient.Builder builder, ChatMemory chatMemory) {
        this.chatClient = builder.build();
        this.chatMemory = chatMemory;
    }

    public String run(String conversationId, String input) {
        return chatClient.prompt()
                .system("""
                        You are a helpful assistant.
                        
                        You MUST use previous conversation context.
                        If the user previously told their name, always use it.
                        
                        Never say you don't know if it exists in memory.
                        """)
                .user(input)
                .advisors(
                        PromptChatMemoryAdvisor.builder(chatMemory)
                                .conversationId(conversationId)
                                .build()
                )
                .call()
                .content();
    }

}
