package com.example.ecommerce_app_with_chathpt.model.dto.request;

import java.util.ArrayList;
import java.util.List;

public class ChatGPTAttributeRequest {

    private String model;
    private List<Message> messages;
    private Double temperature=0.0;

    public ChatGPTAttributeRequest(String model, String prompt, String systemPrompt, String examples) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user",prompt));
        this.messages.add(new Message("system",systemPrompt));
        this.messages.add(new Message("examples",examples));
    }
}
