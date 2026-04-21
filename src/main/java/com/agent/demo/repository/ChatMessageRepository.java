package com.agent.demo.repository;

import com.agent.demo.entity.ChatMessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessageEntity, String> {

    List<ChatMessageEntity> findByConversationIdOrderByCreatedAtAsc(String conversationId);


}
