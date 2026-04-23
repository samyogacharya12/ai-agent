package com.agent.demo.service;

import com.agent.demo.util.ApiTools;
import com.agent.demo.util.FileTools;
import com.agent.demo.util.NoteTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AgentService {

    private final McpHistoryService mcpHistoryService;

    private final McpDocumentService mcpDocumentService;

    private final ChatClient chatClient;
    private final NoteTools noteTools;
    private final ApiTools apiTools;
    private final FileTools fileTools;

    private final List<ToolCallback> mcpToolCallbacks;


    public AgentService(ChatClient chatClient,
                        NoteTools noteTools,
                        ApiTools apiTools,
                        FileTools fileTools,
                        List<ToolCallback> mcpToolCallbacks,
                        McpDocumentService mcpDocumentService,
                        McpHistoryService mcpHistoryService) {
        this.chatClient = chatClient;
        this.noteTools = noteTools;
        this.apiTools = apiTools;
        this.fileTools = fileTools;
        this.mcpToolCallbacks = mcpToolCallbacks;
        this.mcpDocumentService = mcpDocumentService;
        this.mcpHistoryService = mcpHistoryService;
    }

    public String ask(String conversationId, String userMessage) {

        String lower = userMessage.toLowerCase();

        // 🔥 MCP manual routing
        if (lower.contains("history")) {
            return mcpHistoryService.getConversationHistory(conversationId);
        }

        if (lower.contains("document") || lower.contains("search")) {
            return mcpDocumentService.searchDocument(conversationId, userMessage);
        }

        // fallback → normal LLM
        return chatClient.prompt()
                .system("You are a helpful assistant.")
                .user(userMessage)
                .call()
                .content();
    }


//    public String ask(String userMessage) {
//        return chatClient.prompt()
//            .system("""
//                You are a helpful Spring Boot AI agent.
//                Use tools when the user asks to save notes, read files, or fetch external API data.
//                Do not invent tool results.
//                """)
//            .user(userMessage)
//            .toolCallbacks(mcpToolCallbacks)
//            .call()
//            .content();
//    }

}
