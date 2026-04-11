package com.agent.demo.service;

import com.agent.demo.util.UtilityTools;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.client.ChatClient;

@Service
public class AssistantAgent {

    private final ChatClient chatClient;

    private final UtilityTools utilityTools;

    private final ChatMemory chatMemory;

    public AssistantAgent(ChatClient.Builder builder,
                          UtilityTools utilityTools,
                          ChatMemory chatMemory) {
        this.chatClient = builder.build();
        this.utilityTools = utilityTools;
        this.chatMemory = chatMemory;
    }

    public String run(String conversationId, String userInput) {
        return chatClient.prompt()
                .system("""
                        You are a helpful backend engineering assistant.
                        Answer clearly.
                        If a tool is available and needed, use it.
                        """)
                .user(userInput)
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory).conversationId(conversationId).build())
                .tools(utilityTools)
                .call()
                .content();
    }


}
