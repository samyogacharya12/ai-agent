package com.agent.demo.repository;

import com.agent.demo.entity.UserNote;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserNoteRepository extends MongoRepository<UserNote, String> {

    List<UserNote> findByUserId(String userId);
}
