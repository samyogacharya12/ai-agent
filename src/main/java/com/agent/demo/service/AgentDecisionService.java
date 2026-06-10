package com.agent.demo.service;

import com.agent.demo.dto.ToolDecision;
import com.agent.demo.entity.AgentDecisionLog;
import com.agent.demo.enumconstant.ToolType;
import com.agent.demo.repository.AgentDecisionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AgentDecisionService {

    private final AgentDecisionRepository repository;


    private final KnowledgeAgent knowledgeAgent;
    private final ChatAgent generalChatAgent;


    public AgentDecisionService(AgentDecisionRepository repository,
                                KnowledgeAgent knowledgeAgent,
                                ChatAgent generalChatAgent) {
        this.repository = repository;
        this.knowledgeAgent = knowledgeAgent;
        this.generalChatAgent = generalChatAgent;
    }

    public Agent route(ToolType toolType) {
        return switch (toolType) {
            case KNOWLEDGE_SEARCH -> knowledgeAgent;
            case NONE, WEATHER_SEARCH -> generalChatAgent;
            default -> null;
        };
    }


    public void saveDecision(
            String conversationId,
            String question,
            ToolDecision decision
    ){

        AgentDecisionLog log =
                new AgentDecisionLog();

        log.setConversationId(conversationId);

        log.setQuestion(question);

        log.setSelectedTool(
                decision.tool()
        );

        log.setReason(
                decision.reason()
        );

        log.setCreatedAt(
                Instant.now()
        );


        repository.save(log);

    }

}
