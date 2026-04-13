package com.agent.demo.util;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class FileTools {


    @Tool(description = "Read a text file from a safe local path and return its contents.")
    public String readTextFile(String filePath) {
        try {
            Path path = Path.of(filePath).normalize();

            if (!Files.exists(path)) {
                return "File not found: " + filePath;
            }

            if (!Files.isRegularFile(path)) {
                return "Path is not a regular file: " + filePath;
            }

            return Files.readString(path);
        } catch (IOException e) {
            return "Failed to read file: " + e.getMessage();
        }
    }
}
