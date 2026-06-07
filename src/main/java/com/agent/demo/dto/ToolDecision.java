package com.agent.demo.dto;

import com.agent.demo.enumconstant.ToolType;

public record ToolDecision(
        ToolType tool,

        String reason
) {
}
