package com.agent.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class ReviewerAgent {


    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public ReviewerAgent(ChatClient.Builder builder, ChatMemory chatMemory) {
        this.chatClient = builder.build();
        this.chatMemory = chatMemory;
    }


    public String review(String conversationId, String userInput, String answer) {
        return chatClient.prompt()
                .system("""
                        You are a reviewer agent.
                        
                        Your job is to improve the draft answer.
                        
                        Rules:
                        - Fix incorrect or awkward technical statements
                        - Remove repetition
                        - Improve clarity
                        - Keep the answer practical and concise
                        - Return only the improved final answer
                        """)
                .user("""
                        User request:
                        """ + userInput + """
                        
                        Answer:
                        """ + answer)
                .advisors(
                        PromptChatMemoryAdvisor.builder(chatMemory)
                                .conversationId(conversationId)
                                .build()
                )
                .call()
                .content();
    }

}
