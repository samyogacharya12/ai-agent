package com.agent.demo.service;

import com.agent.demo.dto.ToolDecision;
import com.agent.demo.entity.AgentDecisionLog;
import com.agent.demo.repository.AgentDecisionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AgentDecisionService {

    private final AgentDecisionRepository repository;


    public AgentDecisionService(AgentDecisionRepository repository) {
        this.repository = repository;
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
