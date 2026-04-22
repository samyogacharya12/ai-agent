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

    private final ChatClient chatClient;
    private final NoteTools noteTools;
    private final ApiTools apiTools;
    private final FileTools fileTools;

    private final List<ToolCallback> mcpToolCallbacks;


    public AgentService(ChatClient chatClient,
                        NoteTools noteTools,
                        ApiTools apiTools,
                        FileTools fileTools,
                        List<ToolCallback> mcpToolCallbacks) {
        this.chatClient = chatClient;
        this.noteTools = noteTools;
        this.apiTools = apiTools;
        this.fileTools = fileTools;
        this.mcpToolCallbacks = mcpToolCallbacks;
    }

    public String ask(String userMessage) {
        return chatClient.prompt()
            .system("""
                You are a helpful Spring Boot AI agent.
                Use tools when the user asks to save notes, read files, or fetch external API data.
                Do not invent tool results.
                """)
            .user(userMessage)
            .toolCallbacks(mcpToolCallbacks)
            .call()
            .content();
    }

}
