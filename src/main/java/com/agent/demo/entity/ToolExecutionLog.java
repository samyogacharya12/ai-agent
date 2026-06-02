package com.agent.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "tool_execution_logs")
@Data
public class ToolExecutionLog {

    @Id
    private String id;

    private String conversationId;
    private String toolName;
    private String input;
    private String output;
    private boolean success;
    private String errorMessage;
    private Instant createdAt;

    public ToolExecutionLog() {
    }

    public ToolExecutionLog(
            String conversationId,
            String toolName,
            String input,
            String output,
            boolean success,
            String errorMessage,
            Instant createdAt
    ) {
        this.conversationId = conversationId;
        this.toolName = toolName;
        this.input = input;
        this.output = output;
        this.success = success;
        this.errorMessage = errorMessage;
        this.createdAt = createdAt;
    }
}
