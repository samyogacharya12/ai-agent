package com.agent.demo.controller;


import com.agent.demo.service.RagDocumentService;
import com.agent.demo.service.RagService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagDocumentService ragDocumentService;
    private final RagService ragService;

    public RagController(RagDocumentService ragDocumentService, RagService ragService) {
        this.ragDocumentService = ragDocumentService;
        this.ragService = ragService;
    }

    @PostMapping("/ingest")
    public String ingest(@RequestBody IngestRequest request) {
        return ragDocumentService.ingestDocument(request.filePath());
    }

    @PostMapping("/ask")
    public String ask(@RequestBody AskRequest request) {
        return ragService.ask(request.question());
    }

    public record IngestRequest(@NotNull String filePath) {}
    public record AskRequest(@NotNull String question) {}
}
