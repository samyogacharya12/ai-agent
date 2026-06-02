package com.agent.demo.service;


import com.agent.demo.enumconstant.ChatRole;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

@Service
public class CoordinatorAgent {

    private final ChatClient chatClient;

    private final MongoChatHistoryService mongoChatHistoryService;

    private final ToolBasedService toolBasedService;


    public CoordinatorAgent(ChatClient.Builder chatClientBuilder,
                            ToolCallbackProvider toolCallbackProvider,
                            MongoChatHistoryService mongoChatHistoryService,
                            ToolBasedService toolBasedService) {
        this.chatClient = chatClientBuilder
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
        this.mongoChatHistoryService = mongoChatHistoryService;
        this.toolBasedService = toolBasedService;
    }


    public String run(
            String conversationId,
            String question
    ) {

        String memory =
                mongoChatHistoryService.loadMemory(conversationId);



        String toolResult = "No tool used";


        // Simple tool routing logic
        if(
                question.toLowerCase().contains("search")
                        ||
                        question.toLowerCase().contains("document")
                        ||
                        question.toLowerCase().contains("knowledge")
        ){

            toolResult =
                    toolBasedService
                            .searchKnowledgeBase(
                                    conversationId,
                                    question
                            );
        }

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
                                """.formatted(memory, toolResult))


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
