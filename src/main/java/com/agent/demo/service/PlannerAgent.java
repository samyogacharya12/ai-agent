package com.agent.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class PlannerAgent {

    private final ChatMemory chatMemory;

    private final ChatClient chatClient;

    public PlannerAgent(ChatClient.Builder builder,
                        ChatMemory chatMemory) {
        this.chatClient = builder.build();
        this.chatMemory = chatMemory;
    }

    public String createPlan(String conversationId, String userInput) {
        return chatClient.prompt()
                .system("""
                    You are a planning agent.

                    Your task is to return only a short execution plan.

                    Rules:
                    - Return exactly 3 numbered steps
                    - Each step must be one short sentence
                    - Do not answer the user's request directly
                    - Do not explain anything
                    - Do not add introduction or conclusion
                    """)
                .user(userInput)
                .advisors(
                        PromptChatMemoryAdvisor.builder(chatMemory)
                                .conversationId(conversationId)
                                .build()
                )
                .call()
                .content();
    }
}
