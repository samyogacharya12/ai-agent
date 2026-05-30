package com.agent.demo.util;

import com.agent.demo.service.KnowledgeBaseService;
import org.springframework.stereotype.Service;

@Service
public class McpToolService {

    private final KnowledgeBaseService knowledgeBaseService;

    public McpToolService(KnowledgeBaseService knowledgeBaseService) {
        this.knowledgeBaseService = knowledgeBaseService;
    }

    public String searchKnowledgeBase(String question) {
        return knowledgeBaseService.retrieveRelevantChunks(question);
    }


}
