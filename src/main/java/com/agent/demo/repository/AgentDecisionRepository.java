package com.agent.demo.repository;


import com.agent.demo.entity.AgentDecisionLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentDecisionRepository extends MongoRepository<AgentDecisionLog,String> {

    List<AgentDecisionLog>
    findByConversationId(String conversationId);

}
