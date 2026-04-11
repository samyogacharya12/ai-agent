package com.agent.demo.service;


import com.agent.demo.dto.AgentResponse;
import org.springframework.stereotype.Service;

@Service
public class CoordinatorAgent {

    private final PlannerAgent plannerAgent;
    private final ExecutorAgent executorAgent;

    public CoordinatorAgent(PlannerAgent plannerAgent, ExecutorAgent executorAgent) {
        this.plannerAgent = plannerAgent;
        this.executorAgent = executorAgent;
    }

    public AgentResponse run(String conversationId, String userInput) {
        String plan = plannerAgent.createPlan(conversationId, userInput);
        String answer = executorAgent.execute(conversationId,userInput, plan);
        return new AgentResponse(plan, answer);
    }
}
