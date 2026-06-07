package com.agent.demo.dto;

import com.agent.demo.enumconstant.ToolType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentResponse {

    private String plan;
    private String answer;


    private ToolType toolUsed;

    private String reason;


    public AgentResponse(ToolType toolUsed,
                         String answer,
                         String reason) {
        this.toolUsed = toolUsed;
        this.answer = answer;
        this.reason = reason;
    }
}
