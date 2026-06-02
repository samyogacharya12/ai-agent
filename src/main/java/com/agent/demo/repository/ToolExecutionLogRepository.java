package com.agent.demo.repository;

import com.agent.demo.entity.ToolExecutionLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ToolExecutionLogRepository extends MongoRepository<ToolExecutionLog, String> {

    List<ToolExecutionLog> findByConversationIdOrderByCreatedAtDesc(
            String conversationId
    );

    void deleteByConversationId(String conversationId);
}
