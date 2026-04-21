package com.agent.demo.service;

import com.agent.demo.entity.ChatMessageEntity;
import com.agent.demo.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MongoChatHistoryService {


    private final ChatMessageRepository repository;

    public MongoChatHistoryService(ChatMessageRepository repository) {
        this.repository = repository;
    }

    public void saveUserMessage(String conversationId, String content) {
        repository.save(new ChatMessageEntity(conversationId, "USER", content, Instant.now()));
    }

    public void saveAssistantMessage(String conversationId, String content) {
        repository.save(new ChatMessageEntity(conversationId, "ASSISTANT", content, Instant.now()));
    }

    public List<ChatMessageEntity> getConversationHistory(String conversationId) {
        return repository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    public String getRecentHistoryAsText(String conversationId, int limit) {
        List<ChatMessageEntity> history = repository.findByConversationIdOrderByCreatedAtAsc(conversationId);

        int start = Math.max(0, history.size() - limit);

        StringBuilder sb = new StringBuilder();
        for (int i = start; i < history.size(); i++) {
            ChatMessageEntity msg = history.get(i);
            sb.append(msg.getRole()).append(": ").append(msg.getContent()).append("\n");
        }
        return sb.toString();
    }
}
