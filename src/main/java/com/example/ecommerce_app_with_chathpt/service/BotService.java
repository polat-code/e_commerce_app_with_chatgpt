package com.example.ecommerce_app_with_chathpt.service;


import com.example.ecommerce_app_with_chathpt.config.OpenAIConfig;

import com.example.ecommerce_app_with_chathpt.model.dto.request.ChatGPTRequest;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ChatGPTResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BotService {

    private RestTemplate restTemplate;

    private OpenAIConfig openAIConfig;

    private CategoryService categoryService;

    private ChatGPTService chatGPTService;

    private SearchService searchService;

    public String intentExtraction(String message) throws JsonProcessingException {
        String manipulatedMessage ="These are intents of my sentences = [search, login, register, buy,other]. If intent of message i will give you is one of these return just intent of that sentence. Sentence = " + message;
        ChatGPTRequest request = new ChatGPTRequest(openAIConfig.getOpenai_model(),manipulatedMessage);
        ChatGPTResponse chatGPTResponse = restTemplate.postForObject(openAIConfig.getOpenai_api_url(), request, ChatGPTResponse.class);
        String intent = chatGPTResponse.getChoices().get(0).getMessage().getContent();
        intentDirector(intent, message);
        System.out.println(intent);
        //return chatGPTResponse.getChoices().get(0).getMessage().getContent();
        return intent;
    }


    private ResponseEntity<String> intentDirector(String intent, String message) throws JsonProcessingException {
        String category;

        if (intent.equals("login")){


        }
        else if(intent.equals("search"))
        {

           searchService.searchByRequest(message);

        }
        else if(intent.equals("buy"))
        {

        }
        else if(intent.equals("register"))
        {

        }
        else if(intent.equals("other"))
        {

        }
        else {
            throw new RuntimeException();
        }


        return new ResponseEntity<>("OK", HttpStatus.OK);
    }



    public String subcategoryExtraction(String message, String categoryId){

        /*List<Category> categoryList = categoryService.getChildCategoriesOfParent(Integer.parseInt(categoryId));

        String manipulatedMessage ="These are subcategories of my system "+ categoryList.toString()+"If user want to buy on one of these sub categories return just subcategoryId of that request. Sentence = "+ message;
        ChatGPTRequest request = new ChatGPTRequest(openAIConfig.getOpenai_model(),manipulatedMessage);
        ChatGPTResponse chatGPTResponse = restTemplate.postForObject(openAIConfig.getOpenai_api_url(), request, ChatGPTResponse.class);
        attributeExtraction(message);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();*/
        return "x";
    }

    public String attributeExtraction(String message){

        List<String> attributeList = new ArrayList<>();

        String manipulatedMessage ="These are attributes of this desired product "+attributeList.toString()+"If the sentence contains values of these attributes return me with filling them as just  json file. Sentence = "+ message;
        ChatGPTRequest request = new ChatGPTRequest(openAIConfig.getOpenai_model(),manipulatedMessage);
        ChatGPTResponse chatGPTResponse = restTemplate.postForObject(openAIConfig.getOpenai_api_url(), request, ChatGPTResponse.class);
        System.out.println(chatGPTResponse.getChoices().get(0).getMessage().getContent());
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();


    }


}
