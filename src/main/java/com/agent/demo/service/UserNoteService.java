package com.agent.demo.service;

import com.agent.demo.entity.UserNote;
import com.agent.demo.repository.UserNoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserNoteService {

    private final UserNoteRepository repository;

    public UserNoteService(UserNoteRepository repository) {
        this.repository = repository;
    }

    public UserNote saveNote(String userId, String note) {
        return repository.save(new UserNote(userId, note));
    }

    public List<UserNote> getNotesByUserId(String userId) {
        return repository.findByUserId(userId);
    }

}
