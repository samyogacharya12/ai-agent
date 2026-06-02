package com.agent.demo.service;

import com.agent.demo.entity.ToolExecutionLog;
import com.agent.demo.repository.ToolExecutionLogRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ToolExecutionLogService {

    private final ToolExecutionLogRepository repository;

    public ToolExecutionLogService(
            ToolExecutionLogRepository repository
    ) {
        this.repository = repository;
    }



    public void saveSuccess(
            String conversationId,
            String toolName,
            String input,
            String output
    ) {
        repository.save(
                new ToolExecutionLog(
                        conversationId,
                        toolName,
                        input,
                        output,
                        true,
                        null,
                        Instant.now()
                )
        );
    }

    public void saveFailure(
            String conversationId,
            String toolName,
            String input,
            String errorMessage
    ) {
        repository.save(
                new ToolExecutionLog(
                        conversationId,
                        toolName,
                        input,
                        null,
                        false,
                        errorMessage,
                        Instant.now()
                )
        );
    }

    public List<ToolExecutionLog> getLogs(
            String conversationId
    ) {
        return repository
                .findByConversationIdOrderByCreatedAtDesc(conversationId);
    }

    public void clearLogs(
            String conversationId
    ) {
        repository.deleteByConversationId(conversationId);
    }
}