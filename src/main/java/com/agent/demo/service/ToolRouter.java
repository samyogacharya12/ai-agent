package com.agent.demo.service;

import com.agent.demo.dto.ToolDecision;
import com.agent.demo.enumconstant.ToolType;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ToolRouter {

    private final ChatClient chatClient;


    public ToolRouter(
            ChatClient.Builder builder
    ) {

        this.chatClient = builder.build();
    }

    public ToolType route(String message) {
        String text = message.toLowerCase();

        if (text.contains("document")
                || text.contains("pdf")
                || text.contains("rag")
                || text.contains("search")
                || text.contains("file")
                || text.contains("mcp")
                || text.contains("cryptography")
                || text.contains("advisor")) {
            return ToolType.RAG_SEARCH;
        }

        if (text.contains("history")
                || text.contains("previous")
                || text.contains("conversation")
                || text.contains("chat before")) {
            return ToolType.CONVERSATION_HISTORY;
        }

        return ToolType.GENERAL_CHAT;
    }



    public ToolDecision decide(
            String question
    ){


        String response =
                chatClient.prompt()

                        .system("""
                You are a tool routing agent.

                Decide which tool should answer.

                Available tools:

                KNOWLEDGE_SEARCH:
                - documents
                - stored knowledge
                - technical references

                NONE:
                - normal conversation

                Return only:

                KNOWLEDGE_SEARCH

                or

                NONE
                """)

                        .user(question)

                        .call()

                        .content();


        if(response.contains("KNOWLEDGE_SEARCH")){

            return new ToolDecision(
                    ToolType.KNOWLEDGE_SEARCH,
                    "Requires knowledge search"
            );
        }


        return new ToolDecision(
                ToolType.NONE,
                "No tool needed"
        );
    }

}
