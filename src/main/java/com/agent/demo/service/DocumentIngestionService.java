package com.agent.demo.service;

import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DocumentIngestionService {

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public boolean documentExists(
            String filename,
            String conversationId
    ) {

        Integer count =
                jdbcTemplate.queryForObject(
                        """
                        SELECT COUNT(*)
                        FROM vector_store
                        WHERE metadata->>'source' = ?
                        AND metadata->>'conversationId' = ?
                        """,
                        Integer.class,
                        filename,
                        conversationId
                );

        return count != null && count > 0;
    }

}
