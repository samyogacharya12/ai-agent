package com.agent.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_notes")
@Data
public class UserNote {

    @Id
    private String id;
    private String userId;
    private String note;


    public UserNote(String userId, String note) {
        this.userId = userId;
        this.note = note;
    }
}
