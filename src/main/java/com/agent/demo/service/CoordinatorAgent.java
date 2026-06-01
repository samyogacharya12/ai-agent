package com.agent.demo.service;


import com.agent.demo.enumconstant.ChatRole;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

@Service
public class CoordinatorAgent {

    private final ChatClient chatClient;

    private final MongoChatHistoryService mongoChatHistoryService;

    public CoordinatorAgent(ChatClient.Builder chatClientBuilder,
                            ToolCallbackProvider toolCallbackProvider,
                            MongoChatHistoryService mongoChatHistoryService) {
        this.chatClient = chatClientBuilder
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
        this.mongoChatHistoryService = mongoChatHistoryService;
    }


    public String run(
            String conversationId,
            String question
    ) {

        String memory =
                mongoChatHistoryService.loadMemory(conversationId);


        String response =
                chatClient.prompt()

                        .system("""
                                You are an AI assistant.
                                
                                Previous conversation:
                                %s
                                
                                Rules:
                                1. Use memory when useful.
                                2. Use MCP tools for document questions.
                                3. Never hallucinate.
                                """.formatted(memory))


                        .user(question)

                        .call()

                        .content();


        mongoChatHistoryService.saveMessage(
                conversationId,
                ChatRole.USER,
                question
        );


        mongoChatHistoryService.saveMessage(
                conversationId,
                ChatRole.ASSISTANT,
                response
        );


        return response;

    }
}
