package com.example.ecommerce_app_with_chathpt.service;


import com.example.ecommerce_app_with_chathpt.config.OpenAIConfig;
import com.example.ecommerce_app_with_chathpt.model.Category;
import com.example.ecommerce_app_with_chathpt.model.dto.request.ChatGPTRequest;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ChatGPTResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatGPTService {


    private OpenAIConfig openAIConfig;
    private RestTemplate restTemplate;


    public String categoryExtraction(String message){


        ChatGPTRequest request = new ChatGPTRequest(openAIConfig.getOpenai_model(), message);
        ChatGPTResponse chatGPTResponse = restTemplate.postForObject(openAIConfig.getOpenai_api_url(), request, ChatGPTResponse.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }
}
