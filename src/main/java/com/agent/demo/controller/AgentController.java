package com.agent.demo.controller;

import com.agent.demo.dto.AgentResponse;
import com.agent.demo.dto.ParamDto;

import com.agent.demo.service.CoordinatorAgent;
import com.agent.demo.service.MemoryTestAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    @Autowired
    private CoordinatorAgent coordinatorAgent;

    @Autowired
    private MemoryTestAgent memoryTestAgent;

    @PostMapping("/ask")
    public AgentResponse ask(@RequestBody ParamDto paramDto) {
        return coordinatorAgent.run(paramDto.getConversationId(),
                paramDto.getInput());
    }

    @PostMapping("/memory/ask")
    public String askMemory(@RequestBody ParamDto paramDto) {
        return memoryTestAgent.run(paramDto.getConversationId(),
                paramDto.getInput());
    }
}
