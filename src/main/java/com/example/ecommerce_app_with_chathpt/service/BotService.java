package com.example.ecommerce_app_with_chathpt.service;


import com.example.ecommerce_app_with_chathpt.config.OpenAIConfig;
import com.example.ecommerce_app_with_chathpt.model.Category;
import com.example.ecommerce_app_with_chathpt.model.dto.request.ChatGPTRequest;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ChatGPTResponse;
import lombok.AllArgsConstructor;
import lombok.Value;
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




    public String intentExtraction(String message){
        String manipulatedMessage ="These are intents of my sentences = [search, login, register, buy,other]. If intent of message i will give you is one of these return just intent of that sentence. Sentence = " + message;
        ChatGPTRequest request = new ChatGPTRequest(openAIConfig.getOpenai_model(),manipulatedMessage);
        //ChatGPTResponse chatGPTResponse = restTemplate.postForObject(apiUrl, request, ChatGPTResponse.class);
        //String intent = chatGPTResponse.getChoices().get(0).getMessage().getContent();
        intentDirector("search", message);
        //return chatGPTResponse.getChoices().get(0).getMessage().getContent();
        return "search";
    }


    private String intentDirector(String intent, String message){
        String category;

        if (true){
            category = categoryExtraction(message);


        }
        else {
            System.out.println("Intent error " + intent);
            category = "Not wanted";
        }

        return category;
    }
    public String categoryExtraction(String message){

        List<Category> categoryList = categoryService.getParentCategories();

        String manipulatedMessage ="These are categories of my system "+ categoryList.toString()+"If user want to buy on one of these main categories return just category_id of that request. Sentence = "+ message;
        ChatGPTRequest request = new ChatGPTRequest(openAIConfig.getOpenai_model(), manipulatedMessage);
        ChatGPTResponse chatGPTResponse = restTemplate.postForObject(openAIConfig.getOpenai_api_url(), request, ChatGPTResponse.class);
        System.out.println(chatGPTResponse);
        subcategoryExtraction(message, chatGPTResponse.getChoices().get(0).getMessage().getContent());
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }


    public String subcategoryExtraction(String message, String categoryId){

        List<Category> categoryList = categoryService.getChildCategoriesOfParent(Integer.parseInt(categoryId));

        String manipulatedMessage ="These are subcategories of my system "+ categoryList.toString()+"If user want to buy on one of these sub categories return just subcategoryId of that request. Sentence = "+ message;
        ChatGPTRequest request = new ChatGPTRequest(openAIConfig.getOpenai_model(),manipulatedMessage);
        ChatGPTResponse chatGPTResponse = restTemplate.postForObject(openAIConfig.getOpenai_api_url(), request, ChatGPTResponse.class);
        attributeExtraction(message);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();

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
