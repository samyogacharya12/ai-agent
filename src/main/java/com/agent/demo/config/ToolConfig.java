//package com.agent.demo.config;
//
//import com.agent.demo.util.RagTools;
//import org.springframework.ai.tool.ToolCallbackProvider;
//import org.springframework.ai.tool.method.MethodToolCallbackProvider;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ToolConfig {
//
//
//    @Bean
//    public ToolCallbackProvider toolCallbackProvider(RagTools ragTools) {
//        return MethodToolCallbackProvider.builder()
//                .toolObjects(ragTools)
//                .build();
//    }
//}
