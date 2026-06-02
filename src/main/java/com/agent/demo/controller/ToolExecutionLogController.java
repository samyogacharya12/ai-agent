package com.agent.demo.controller;

import com.agent.demo.entity.ToolExecutionLog;
import com.agent.demo.service.ToolExecutionLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tools/logs")
public class ToolExecutionLogController {

    private final ToolExecutionLogService logService;

    public ToolExecutionLogController(
            ToolExecutionLogService logService
    ) {
        this.logService = logService;
    }

    @GetMapping("/{conversationId}")
    public List<ToolExecutionLog> getLogs(
            @PathVariable String conversationId
    ) {
        return logService.getLogs(conversationId);
    }

    @DeleteMapping("/{conversationId}")
    public String clearLogs(
            @PathVariable String conversationId
    ) {
        logService.clearLogs(conversationId);
        return "Tool execution logs cleared successfully";
    }
}