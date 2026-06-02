package com.agent.demo.service;

import com.agent.demo.enumconstant.ToolType;
import com.agent.demo.util.ApiTools;
import com.agent.demo.util.FileTools;
import com.agent.demo.util.McpToolExecutor;
import com.agent.demo.util.NoteTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AgentService {

    private final McpHistoryService mcpHistoryService;

    private final McpDocumentService mcpDocumentService;

    private final ChatClient chatClient;
    private final NoteTools noteTools;
    private final ApiTools apiTools;
    private final FileTools fileTools;

    private final ToolRouter toolRouter;

    private final McpClient mcpClient;


    private final List<ToolCallback> mcpToolCallbacks;


    private final ToolExecutionLogService toolExecutionLogService;


    private final McpToolExecutor mcpToolExecutor;


    public AgentService(ChatClient chatClient,
                        NoteTools noteTools,
                        ApiTools apiTools,
                        FileTools fileTools,
                        List<ToolCallback> mcpToolCallbacks,
                        McpDocumentService mcpDocumentService,
                        McpHistoryService mcpHistoryService,
                        ToolRouter toolRouter,
                        McpClient mcpClient,
                        McpToolExecutor mcpToolExecutor,
                        ToolExecutionLogService toolExecutionLogService) {
        this.chatClient = chatClient;
        this.noteTools = noteTools;
        this.apiTools = apiTools;
        this.fileTools = fileTools;
        this.mcpToolCallbacks = mcpToolCallbacks;
        this.mcpDocumentService = mcpDocumentService;
        this.mcpHistoryService = mcpHistoryService;
        this.toolRouter = toolRouter;
        this.mcpClient = mcpClient;
        this.mcpToolExecutor = mcpToolExecutor;
        this.toolExecutionLogService = toolExecutionLogService;
    }

    public String ask(String conversationId, String message) {

        ToolType toolType = toolRouter.route(message);

        if (toolType == ToolType.GENERAL_CHAT) {
            return chatClient.prompt()
                    .user(message)
                    .call()
                    .content();
        }

        String toolResponse = mcpToolExecutor.execute(
                toolType,
                conversationId,
                message
        );

        return chatClient.prompt()
                .system("""
                        You are an AI assistant.
                        Use the MCP tool response to answer clearly.
                        If the tool response is not enough, say that clearly.
                        """)
                .user("""
                        User question:
                        %s

                        MCP tool response:
                        %s
                        """.formatted(message, toolResponse))
                .call()
                .content();
    }


    public String handleMessage(String message, String conversationId) {
        ToolType toolType = toolRouter.route(message);

        return switch (toolType) {
            case RAG_SEARCH -> {
                String context = mcpClient.askRag(conversationId, message);
                yield context;
            }

            case CONVERSATION_HISTORY -> {
                String history = mcpClient.getConversationHistory(conversationId);
                yield askLlmWithContext(message, history);
            }

            case GENERAL_CHAT -> chatClient
                    .prompt()
                    .user(message)
                    .call()
                    .content();
        };
    }

    private String askLlmWithContext(String question, String context) {
        return chatClient
                .prompt()
                .system("""
                        You are an AI assistant.
                        Use the provided context to answer the user.
                        If the context is not enough, say that clearly.
                        """)
                .user("""
                        Context:
                        %s

                        Question:
                        %s
                        """.formatted(context, question))
                .call()
                .content();
    }


}
