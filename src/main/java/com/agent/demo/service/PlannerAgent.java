package com.agent.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class PlannerAgent {

    private final ChatClient chatClient;

    public PlannerAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String createPlan(String userInput) {
        return chatClient.prompt()
                .system("""
                        You are a planning agent.
                        Break the user request into 3 short actionable steps.
                        Do not solve the task.
                        """)
                .user(userInput)
                .call()
                .content();
    }
}
