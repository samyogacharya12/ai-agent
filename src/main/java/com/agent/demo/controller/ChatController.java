package com.agent.demo.controller;

import com.agent.demo.dto.AgentResponse;
import com.agent.demo.dto.ParamDto;
import com.agent.demo.service.CoordinatorAgent;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final CoordinatorAgent coordinatorAgent;

    public ChatController(CoordinatorAgent coordinatorAgent) {
        this.coordinatorAgent = coordinatorAgent;
    }

    @PostMapping
    public AgentResponse ask(@RequestBody ParamDto request) {

        return coordinatorAgent.run(
                request.getConversationId(),
                request.getQuestion()
        );

    }

}
