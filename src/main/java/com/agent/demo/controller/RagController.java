package com.agent.demo.controller;


import com.agent.demo.dto.ParamDto;
import com.agent.demo.service.MongoChatHistoryService;
import com.agent.demo.service.RagDocumentService;
import com.agent.demo.service.RagService;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagDocumentService ragDocumentService;
    private final RagService ragService;

    private final MongoChatHistoryService mongoChatHistoryService;


    public RagController(RagDocumentService ragDocumentService,
                         RagService ragService,
                         MongoChatHistoryService mongoChatHistoryService) {
        this.ragDocumentService = ragDocumentService;
        this.ragService = ragService;
        this.mongoChatHistoryService=mongoChatHistoryService;
    }

    @PostMapping("/ingest")
    public String ingest(@RequestBody IngestRequest request) {
        return ragDocumentService.ingestDocument(request.filePath());
    }

    @PostMapping("/ask")
    public String ask(@RequestBody ParamDto request) {
        return ragService.
            ask(request.getConversationId(),
                request.getQuestion());
    }

    @PostMapping("/ask-debug")
    public RagService.RagDebugResponse askDebug(@RequestBody AskRequest request) {
        return ragService.askWithDebug(request.conversationId(), request.question());
    }

    @GetMapping("/history/{conversationId}")
    public Object history(@PathVariable String conversationId) {
        return mongoChatHistoryService.getConversationHistory(conversationId);
    }

    public record IngestRequest(@NotNull String filePath) {}
    public record AskRequest(@NotNull String question, @NotNull String conversationId) {

    }
}
