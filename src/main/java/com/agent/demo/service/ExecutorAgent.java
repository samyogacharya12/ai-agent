package com.agent.demo.service;

import com.agent.demo.util.UtilityTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class ExecutorAgent {

    private final ChatClient chatClient;
    private final UtilityTools utilityTools;
    private final ChatMemory chatMemory;

    public ExecutorAgent(ChatClient.Builder builder, UtilityTools utilityTools,
                         ChatMemory chatMemory) {
        this.chatClient = builder.build();
        this.utilityTools = utilityTools;
        this.chatMemory = chatMemory;
    }

    public String execute(String conversationId, String userInput, String plan) {
        return chatClient.prompt()
                .system("""
                        You are an execution agent.
                        Follow the plan carefully.
                        Use tools if useful.
                        Return the final answer.
                        """)
                .user("User request:\n" + userInput + "\n\nPlan:\n" + plan)
                .tools(utilityTools)
                .advisors(
                        PromptChatMemoryAdvisor.builder(chatMemory)
                                .conversationId(conversationId)
                                .build()
                )
                .call()
                .content();
    }

}
