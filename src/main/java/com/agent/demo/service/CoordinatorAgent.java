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

    private final SharedToolService sharedToolService;


    private final ToolRouter toolRouterAgent;

    private final AgentDecisionService agentDecisionService;

    private final ChatAgent chatAgent;

    public CoordinatorAgent(ChatClient.Builder chatClientBuilder,
                            ToolCallbackProvider toolCallbackProvider,
                            MongoChatHistoryService mongoChatHistoryService,
                            ToolBasedService toolBasedService,
                            ToolRouter toolRouterAgent,
                            AgentDecisionService agentDecisionService,
                            ChatAgent chatAgent,
                            SharedToolService sharedToolService) {
        this.chatClient = chatClientBuilder
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
        this.mongoChatHistoryService = mongoChatHistoryService;
        this.toolBasedService = toolBasedService;
        this.toolRouterAgent = toolRouterAgent;
        this.agentDecisionService = agentDecisionService;
        this.chatAgent = chatAgent;
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
                        question
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
