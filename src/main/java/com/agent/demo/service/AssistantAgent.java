package com.agent.demo.service;

import com.agent.demo.util.UtilityTools;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.client.ChatClient;
@Service
public class AssistantAgent {

    private final ChatClient chatClient;

    private final UtilityTools utilityTools;

    public AssistantAgent(ChatClient.Builder builder,  UtilityTools utilityTools) {
        this.chatClient = builder.build();
        this.utilityTools = utilityTools;
    }

    public String run(String userInput) {
        return chatClient.prompt()
                .system("""
                        You are a helpful backend engineering assistant.
                        Answer clearly.
                        If a tool is available and needed, use it.
                        """)
                .user(userInput)
                .tools(utilityTools)
                .call()
                .content();
    }


}
