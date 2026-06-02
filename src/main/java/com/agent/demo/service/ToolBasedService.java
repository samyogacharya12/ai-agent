package com.agent.demo.service;

import org.springframework.stereotype.Service;

@Service
public class ToolBasedService {

    private final KnowledgeBaseService knowledgeBaseService;
    private final ToolExecutionLogService toolExecutionLogService;

    public ToolBasedService(
            KnowledgeBaseService knowledgeBaseService,
            ToolExecutionLogService toolExecutionLogService
    ) {
        this.knowledgeBaseService = knowledgeBaseService;
        this.toolExecutionLogService = toolExecutionLogService;
    }

    public String searchKnowledgeBase(
            String conversationId,
            String question
    ) {
        try {
            String result = knowledgeBaseService.search(question);

            toolExecutionLogService.saveSuccess(
                    conversationId,
                    "search_knowledge_base",
                    question,
                    result
            );

            return result;

        } catch (Exception e) {

            toolExecutionLogService.saveFailure(
                    conversationId,
                    "search_knowledge_base",
                    question,
                    e.getMessage()
            );

            throw e;
        }
    }





}
