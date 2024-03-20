package com.example.ecommerce_app_with_chathpt.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class SendMessageChatRequest {
    private String chatId;
    private String message;
}
