package com.agent.demo.dto;

import com.agent.demo.enumconstant.ToolType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentResponse {

    private String plan;
    private String answer;


    private ToolType toolUsed;

    private String reason;


    public AgentResponse(String plan,
                         String answer,
                         String reason) {
        this.plan = plan;
        this.answer = answer;
        this.reason = reason;
    }

    public AgentResponse(ToolType toolUsed,
                         String answer,
                         String reason) {
        this.toolUsed = toolUsed;
        this.answer = answer;
        this.reason = reason;
    }
}
