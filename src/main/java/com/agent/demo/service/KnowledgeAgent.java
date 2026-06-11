package com.agent.demo.service;

import com.agent.demo.dto.AgentResponse;
import com.agent.demo.enumconstant.ToolType;
import org.springframework.stereotype.Service;

@Service
public class KnowledgeAgent implements Agent{

    private final SharedToolService sharedToolService;

    public KnowledgeAgent(SharedToolService sharedToolService) {
        this.sharedToolService = sharedToolService;
    }


    @Override
    public AgentResponse execute(String conversationId, String input) {
        String result =
                sharedToolService.executeTool(
                        ToolType.KNOWLEDGE_SEARCH,
                        input,
                        conversationId
                );


        return new AgentResponse(
                ToolType.KNOWLEDGE_SEARCH,
                result,
                "Knowledge agent used shared MCP tool"
        );
    }
}
