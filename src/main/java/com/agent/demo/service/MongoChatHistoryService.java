package com.agent.demo.service;

import com.agent.demo.entity.ChatMessageEntity;
import com.agent.demo.enumconstant.ChatRole;
import com.agent.demo.repository.ChatMessageRepository;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MongoChatHistoryService {


    private final ChatMessageRepository repository;

    private final ChatMemory chatMemory;


    public MongoChatHistoryService(ChatMessageRepository repository,
                                   ChatMemory chatMemory) {
        this.repository = repository;
        this.chatMemory = chatMemory;
    }

    public void saveMessage(String conversationId,ChatRole chatRole ,String content) {
        ChatMessageEntity saved=repository.save(new ChatMessageEntity(conversationId,chatRole, content, Instant.now()));
        System.out.println("Saved message ID: " + saved.getId());
        System.out.println("Saved conversationId: " + saved.getConversationId());
        System.out.println("Saved role: " + saved.getRole());
        System.out.println("Saved content: " + saved.getContent());
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

    public String loadMemory(String conversationId){

        return repository
                .findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(m ->
                        m.getRole()+": "+m.getContent()
                )
                .collect(Collectors.joining("\n"));
    }
}
