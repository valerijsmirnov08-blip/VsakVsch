package com.example.demo.dto;

import java.util.List;

public record BotReplyDTO(
        String reply,
        List<String> quickReplies,
        Long escalatedChatId
) {}