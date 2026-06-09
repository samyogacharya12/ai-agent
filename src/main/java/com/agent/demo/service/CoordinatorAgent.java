package com.agent.demo.service;


import com.agent.demo.dto.AgentResponse;
import com.agent.demo.dto.ToolDecision;
import com.agent.demo.enumconstant.ChatRole;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

@Service
public class CoordinatorAgent {

    private final ChatClient chatClient;

    private final MongoChatHistoryService mongoChatHistoryService;

    private final ToolBasedService toolBasedService;


    private final ToolRouter toolRouterAgent;

    private final  AgentDecisionService agentDecisionService;

    private final ChatAgent chatAgent;

    public CoordinatorAgent(ChatClient.Builder chatClientBuilder,
                            ToolCallbackProvider toolCallbackProvider,
                            MongoChatHistoryService mongoChatHistoryService,
                            ToolBasedService toolBasedService,
                            ToolRouter toolRouterAgent,
                            AgentDecisionService agentDecisionService,
                            ChatAgent chatAgent) {
        this.chatClient = chatClientBuilder
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
        this.mongoChatHistoryService = mongoChatHistoryService;
        this.toolBasedService = toolBasedService;
        this.toolRouterAgent = toolRouterAgent;
        this.agentDecisionService = agentDecisionService;
        this.chatAgent = chatAgent;
    }


    public AgentResponse run(
            String conversationId,
            String question
    ) {

        String memory =
                mongoChatHistoryService.loadMemory(conversationId);



        String toolResult = "No tool used";


        ToolDecision decision =
                toolRouterAgent.decide(question);

        agentDecisionService.saveDecision(
                conversationId,
                question,
                decision
        );

        switch(decision.tool()) {


            case KNOWLEDGE_SEARCH ->

                    toolResult =
                            toolBasedService
                                    .searchKnowledgeBase(
                                            conversationId,
                                            question
                                    );


            case NONE ->
                    chatAgent.execute(conversationId, question);
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


        return new AgentResponse(
                decision.tool(),
                response,
                decision.reason()
        );

    }
}
