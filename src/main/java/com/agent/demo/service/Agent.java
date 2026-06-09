package com.agent.demo.service;

import com.agent.demo.dto.AgentResponse;

public interface Agent {

    AgentResponse execute(
            String conversationId,
            String input
    );
}
