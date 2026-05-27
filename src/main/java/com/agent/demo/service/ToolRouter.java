package com.agent.demo.service;

import com.agent.demo.enumconstant.ToolType;
import org.springframework.stereotype.Service;

@Service
public class ToolRouter {

    public ToolType route(String message) {
        String text = message.toLowerCase();

        if (text.contains("document")
                || text.contains("pdf")
                || text.contains("rag")
                || text.contains("search")
                || text.contains("file")
                || text.contains("mcp")
                || text.contains("cryptography")
                || text.contains("advisor")) {
            return ToolType.RAG_SEARCH;
        }

        if (text.contains("history")
                || text.contains("previous")
                || text.contains("conversation")
                || text.contains("chat before")) {
            return ToolType.CONVERSATION_HISTORY;
        }

        return ToolType.GENERAL_CHAT;
    }
}
