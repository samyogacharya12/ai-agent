package com.agent.demo.service;


import com.agent.demo.dto.AgentResponse;
import com.agent.demo.dto.ToolDecision;
import com.agent.demo.enumconstant.ChatRole;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class CoordinatorAgent {

    private final ChatClient chatClient;

    private final MongoChatHistoryService mongoChatHistoryService;


    private final SharedToolService sharedToolService;


    private final ToolRouter toolRouterAgent;

    private final AgentDecisionService agentDecisionService;


    public CoordinatorAgent(ChatClient.Builder chatClientBuilder,
                            MongoChatHistoryService mongoChatHistoryService,
                            ToolRouter toolRouterAgent,
                            AgentDecisionService agentDecisionService,
                            SharedToolService sharedToolService) {
        this.chatClient = chatClientBuilder
                .build();
        this.mongoChatHistoryService = mongoChatHistoryService;
        this.toolRouterAgent = toolRouterAgent;
        this.agentDecisionService = agentDecisionService;
        this.sharedToolService = sharedToolService;
    }


    public AgentResponse run(
            String conversationId,
            String question
    ) {

        String memory =
                mongoChatHistoryService.loadMemory(conversationId);

        ToolDecision decision =
                toolRouterAgent.decide(question);

        agentDecisionService.saveDecision(
                conversationId,
                question,
                decision
        );

        String toolResult =
                sharedToolService.executeTool(
                        decision.tool(),
                        question,
                        conversationId
                );

        String response =
                chatClient.prompt()
                        .system("""
                        You are an AI assistant.

                        Previous conversation:
                        %s

                        Knowledge/tool result already retrieved:
                        %s

                        Rules:
                        1. Do not output tool calls.
                        2. Do not say you will search.
                        3. Use the provided tool result to answer the user.
                        4. If the tool result says "No matching knowledge found", say you do not know from the uploaded knowledge.
                        5. Answer in normal human language.
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
