package com.agent.demo.service;


import com.agent.demo.dto.AgentResponse;
import org.springframework.stereotype.Service;

@Service
public class CoordinatorAgent {

    private final PlannerAgent plannerAgent;
    private final ExecutorAgent executorAgent;
    private final ReviewerAgent reviewerAgent;

    public CoordinatorAgent(PlannerAgent plannerAgent,
                            ExecutorAgent executorAgent,
                            ReviewerAgent reviewerAgent) {
        this.plannerAgent = plannerAgent;
        this.executorAgent = executorAgent;
        this.reviewerAgent = reviewerAgent;
    }

    public AgentResponse run(String conversationId, String userInput) {
        String plan = plannerAgent.createPlan(conversationId, userInput);
        String answer = executorAgent.execute(conversationId,userInput, plan);
        String finalAnswer = reviewerAgent.review(conversationId, userInput, answer);
        return new AgentResponse(plan, finalAnswer);
    }
}
