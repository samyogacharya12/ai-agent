package com.agent.demo.util;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UtilityTools {

    @Tool(description = "Get the current server time")
    public String currentTime() {
        return LocalDateTime.now().toString();
    }
}
