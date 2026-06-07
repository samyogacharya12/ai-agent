package com.agent.demo.entity;

import com.agent.demo.enumconstant.ToolType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "agent_decision_logs")
@Data
public class AgentDecisionLog {

    @Id
    private String id;

    private String conversationId;

    private String question;

    private ToolType selectedTool;

    private String reason;

    private Instant createdAt;
}
