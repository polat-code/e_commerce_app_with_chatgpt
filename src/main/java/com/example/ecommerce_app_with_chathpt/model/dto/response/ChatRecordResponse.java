package com.example.ecommerce_app_with_chathpt.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRecordResponse {

    private String chatId;
    private List<ChatResponse> chatResponses;

}
