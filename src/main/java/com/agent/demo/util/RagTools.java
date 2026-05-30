package com.agent.demo.util;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class RagTools {

    private final McpToolService mcpToolService;

    public RagTools(McpToolService mcpToolService) {
        this.mcpToolService = mcpToolService;
    }

    @Tool(name = "search_knowledge_base",
            description = "Search uploaded document chunks and return relevant answer")
    public String searchKnowledgeBase(String question) {
        return mcpToolService.searchKnowledgeBase(question);
    }
}
