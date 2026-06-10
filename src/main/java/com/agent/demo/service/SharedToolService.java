package com.agent.demo.service;

import com.agent.demo.enumconstant.ToolType;
import org.springframework.stereotype.Service;

@Service
public class SharedToolService {

    private final KnowledgeBaseService knowledgeBaseService;

    public SharedToolService(KnowledgeBaseService knowledgeBaseService) {
        this.knowledgeBaseService = knowledgeBaseService;
    }

    public String executeTool(
            ToolType toolType,
            String question
    ) {
        return switch (toolType) {
            case KNOWLEDGE_SEARCH ->
                    knowledgeBaseService.searchKnowledgeBase(question);

            case WEATHER_SEARCH ->
                    "Weather tool is not implemented yet.";

            case NONE ->
                    "No tool needed.";

            default ->
                    "Tool not available yet.";
        };
    }
}