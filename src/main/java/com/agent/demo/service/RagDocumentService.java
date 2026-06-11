package com.agent.demo.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Service
public class RagDocumentService {

    private final VectorStore vectorStore;

    public RagDocumentService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public String ingestDocument(MultipartFile file) {
        try {

            String content = new String(
                    file.getBytes(),
                    StandardCharsets.UTF_8
            );

            Document document = new Document(
                    content,
                    Map.of(
                            "filename", file.getOriginalFilename(),
                            "source", "upload"
                    )
            );

            vectorStore.add(List.of(document));

            System.out.println("Document saved into pgvector.");

            TikaDocumentReader reader = new TikaDocumentReader(file.getResource());
            List<Document> documents = reader.read();

            TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(800)
                .withMinChunkSizeChars(350)
                .withMinChunkLengthToEmbed(10)
                .withMaxNumChunks(1000)
                .withKeepSeparator(true)
                .build();

            List<Document> chunks = splitter.apply(documents);

            for (Document chunk : chunks) {
                chunk.getMetadata().put("source", file.getName());
            }

            vectorStore.add(chunks);

            return "Document ingested successfully. Chunks added: " + chunks.size();
        } catch (Exception e) {
            return "Failed to ingest document: " + e.getMessage();
        }
    }
}
