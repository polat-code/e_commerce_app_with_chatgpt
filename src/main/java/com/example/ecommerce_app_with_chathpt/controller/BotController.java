package com.example.ecommerce_app_with_chathpt.controller;


import com.example.ecommerce_app_with_chathpt.config.OpenAIConfig;
import com.example.ecommerce_app_with_chathpt.model.dto.request.ChatGPTRequest;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ChatGPTResponse;
import com.example.ecommerce_app_with_chathpt.service.BotService;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/prompts")
@AllArgsConstructor
public class BotController {


    private BotService botService;

    private OpenAIConfig openAIConfig;

    private RestTemplate restTemplate;



    @GetMapping("/chat")
    public String chat(@RequestBody String prompt) {
        ChatGPTRequest request = new ChatGPTRequest(openAIConfig.getOpenai_model(), prompt);
        ChatGPTResponse chatGPTResponse = restTemplate.postForObject(openAIConfig.getOpenai_api_url(),request, ChatGPTResponse.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }



    @GetMapping("/intent")
    public String intentDetect(@RequestBody String prompt) {

        return  botService.intentExtraction(prompt);
    }

}
