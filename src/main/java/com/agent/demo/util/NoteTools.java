package com.agent.demo.util;

import com.agent.demo.entity.UserNote;
import com.agent.demo.service.UserNoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NoteTools {

    private final UserNoteService userNoteService;

    public NoteTools(UserNoteService userNoteService) {
        this.userNoteService = userNoteService;
    }

    @Tool(description = "Save a note for a user. Input requires userId and note text.")
    public String saveUserNote(String userId, String note) {
        log.info("Saving note for userId={}, note={}", userId, note);
        UserNote saved = userNoteService.saveNote(userId, note);
        return "Saved note with id: " + saved.getId();
    }

    @Tool(description = "Get all notes for a user by userId.")
    public String getUserNotes(String userId) {
        List<UserNote> notes = userNoteService.getNotesByUserId(userId);

        if (notes.isEmpty()) {
            return "No notes found for userId " + userId;
        }

        return notes.stream()
                .map(n -> "- " + n.getNote())
                .collect(Collectors.joining("\n"));
    }

}
