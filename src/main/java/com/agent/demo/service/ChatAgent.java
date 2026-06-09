package com.agent.demo.service;

import com.agent.demo.dto.AgentResponse;
import com.agent.demo.enumconstant.ToolType;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatAgent implements Agent{

    private final ChatClient chatClient;


    public ChatAgent(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    @Override
    public AgentResponse execute(String conversationId, String input) {
        String response =
                chatClient.prompt()
                        .user(input)
                        .call()
                        .content();


        return new AgentResponse(
                ToolType.NONE,
                response,
                "General chat response"
        );
    }
}
