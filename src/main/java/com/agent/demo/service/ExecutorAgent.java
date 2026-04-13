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

                    Your task is to answer the user's request using the given plan.

                    Rules:
                    - Use the plan as guidance
                    - Give a practical final answer
                    - Do not repeat the plan word for word
                    - Do not mention you are an execution agent
                    - Keep the answer under 120 words
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
