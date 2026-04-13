package com.agent.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
