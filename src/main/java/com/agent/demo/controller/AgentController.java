package com.agent.demo.controller;

import com.agent.demo.dto.ParamDto;
import com.agent.demo.service.AssistantAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agent")
public class AgentController {


    @Autowired
    private AssistantAgent assistantAgent;

    @PostMapping("/ask")
    public String ask(@RequestBody ParamDto paramDto) {
        return assistantAgent.run(paramDto.getConversationId(),
                paramDto.getInput());
    }
}
