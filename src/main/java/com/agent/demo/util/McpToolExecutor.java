package com.agent.demo.util;

import com.agent.demo.enumconstant.ToolType;
import com.agent.demo.service.McpClient;
import org.springframework.stereotype.Service;

@Service
public class McpToolExecutor {


    private final McpClient mcpClient;

    public McpToolExecutor(McpClient mcpClient) {
        this.mcpClient = mcpClient;
    }

    public String execute(ToolType toolType, String conversationId, String message) {
        return switch (toolType) {
            case RAG_SEARCH -> mcpClient.askRag(conversationId, message);
            case CONVERSATION_HISTORY -> mcpClient.searchHistory(conversationId, message);
            case GENERAL_CHAT -> "";
        };
    }


}
