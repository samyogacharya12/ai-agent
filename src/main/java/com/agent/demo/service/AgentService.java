package com.agent.demo.service;

import com.agent.demo.util.ApiTools;
import com.agent.demo.util.FileTools;
import com.agent.demo.util.NoteTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
@Service
public class AgentService {

    private final ChatClient chatClient;
    private final NoteTools noteTools;
    private final ApiTools apiTools;
    private final FileTools fileTools;

    public AgentService(ChatClient chatClient,
                        NoteTools noteTools,
                        ApiTools apiTools,
                        FileTools fileTools) {
        this.chatClient = chatClient;
        this.noteTools = noteTools;
        this.apiTools = apiTools;
        this.fileTools = fileTools;
    }

    public String ask(String userMessage) {
        return chatClient.prompt()
                .system("""
                        You are a helpful Spring Boot AI agent.
                        Use tools when the user asks to save notes, read files, or fetch external API data.
                        Do not invent tool results.
                        """)
                .user(userMessage)
                .tools(noteTools, apiTools, fileTools)
                .call()
                .content();
    }

}
