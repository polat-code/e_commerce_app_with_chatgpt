package com.example.ecommerce_app_with_chathpt.controller;


import com.example.ecommerce_app_with_chathpt.config.OpenAIConfig;
import com.example.ecommerce_app_with_chathpt.model.dto.request.ChatGPTRequest;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ChatGPTResponse;
import com.example.ecommerce_app_with_chathpt.service.BotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/prompts")
@AllArgsConstructor
public class BotController {


    private BotService botService;

    private OpenAIConfig openAIConfig;

    private RestTemplate restTemplate;



    @GetMapping("")
    public void chat(@RequestBody String prompt) {


    }





    @GetMapping("/intent")
    public String intentDetect(@RequestBody String prompt) throws JsonProcessingException {

        return  botService.intentExtraction(prompt);
    }

}
