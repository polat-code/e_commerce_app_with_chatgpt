package com.example.ecommerce_app_with_chathpt.service;


import com.example.ecommerce_app_with_chathpt.config.OpenAIConfig;

import com.example.ecommerce_app_with_chathpt.model.*;
import com.example.ecommerce_app_with_chathpt.model.dto.ProductResponse;
import com.example.ecommerce_app_with_chathpt.model.dto.request.ChatGPTRequest;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ChatGPTResponse;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ChatResponse;
import com.example.ecommerce_app_with_chathpt.util.mapper.GptAttributeAndAttributeValuesJsonResponseToMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class BotService {

    private RestTemplate restTemplate;

    private OpenAIConfig openAIConfig;

    private CategoryService categoryService;

    private ChatGPTService chatGPTService;

    private SearchService searchService;

    private SearchStateService searchStateService;

    private ProductSearchService productSearchService;

    private UserChatService userChatService;

    //TODO Give more examples to prompts
    public String intentExtraction(String message) throws JsonProcessingException {
        String manipulatedMessage = "These are intents of my sentences = [search, login, register, buy, other]. If the intent of the message I give you is one of these, return just the intent of that sentence."
                + "\nExample: 'I want to search for books.' Output: search"
                + "\nExample: 'How do I log in to my account?' Output: login"
                + "\nExample: 'I need to register for a new account.' Output: register"
                + "\nExample: 'I'd like to buy a new laptop.' Output: buy"
                + "\nExample: 'Tell me more about your services.' Output: other";

        ChatGPTRequest request = new ChatGPTRequest(openAIConfig.getOpenai_model(),message,manipulatedMessage);
        ChatGPTResponse chatGPTResponse = restTemplate.postForObject(openAIConfig.getOpenai_api_url(), request, ChatGPTResponse.class);
        String intent = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        return intent;
    }


    public String intentExtractionForInitialState(String message) throws JsonProcessingException {
        String manipulatedMessage = "These are intents of my sentences = [search, login, register, other]. If the intent of the message I give you is one of these, return just the intent of that sentence."
                + "\nExample: 'I want to search for books.' Output: search"
                + "\nExample: 'How do I log in to my account?' Output: login"
                + "\nExample: 'I need to register for a new account.' Output: register"
                + "\nExample: 'Tell me more about your services.' Output: other";

        ChatGPTRequest request = new ChatGPTRequest(openAIConfig.getOpenai_model(),message,manipulatedMessage);
        ChatGPTResponse chatGPTResponse = restTemplate.postForObject(openAIConfig.getOpenai_api_url(), request, ChatGPTResponse.class);
        String intent = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        return intent;
    }

    public String intentExtractionForSearchState(String message) throws JsonProcessingException {
        String manipulatedMessage = "These are intents of my sentences = [search, remove feature, add feature, buy product, add to cart]. If the intent of the message I give you is one of these, return just the intent of that sentence."
                + "\nExample: 'I want to search for books.' Output: search"
                + "\nExample: 'I also want to look for red products.' Output: add feature"
                + "\nExample: 'Do not show products with GTX2060' Output: remove feature"
                + "\nExample: 'I want to add fourth product to my cart' Output: remove feature"
                + "\nExample: 'I want to buy second product.' Output: buy product";

        ChatGPTRequest request = new ChatGPTRequest(openAIConfig.getOpenai_model(),message,manipulatedMessage);
        ChatGPTResponse chatGPTResponse = restTemplate.postForObject(openAIConfig.getOpenai_api_url(), request, ChatGPTResponse.class);
        String intent = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        return intent;
    }




    public ChatResponse intentDirectorInitialState(String intent, String message, String chatId) throws JsonProcessingException {
        String category;
        ChatResponse chatEntityResponse = new ChatResponse();

        if (intent.equals("login")){


        }
        else if(intent.equals("search"))
        {

            chatEntityResponse = searchService.searchByRequest(message, chatId);

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


        return chatEntityResponse;
    }

    public ChatResponse intentDirectorSearchState(String intent, String message, String chatId) throws JsonProcessingException {

        ChatResponse chatEntityResponse = new ChatResponse();
        if(intent.equals("search"))
        {

            searchService.searchByRequest(message, chatId);

        }

        else if(intent.equals("remove feature"))
        {
            Optional<UserChat> userChat = userChatService.getUserChatById(chatId);

            List<GptAttributeAndAttributeValuesJsonResponseToMapper> attributeValueList = searchStateService.removeAttributes(message, userChat.get().getAttributeValues());
            List<AttributeValue> currentAttributeValues = userChat.get().getAttributeValues();;
            List<AttributeValue> featuresToBeRemoved = productSearchService.mapAttributeValues(attributeValueList, userChat.get().getCategory());
            currentAttributeValues.removeIf(featuresToBeRemoved::contains);

            userChatService.setAttributeValuesAndCategoryOfChat(chatId,currentAttributeValues,userChat.get().getCategory());
            return productSearchService.searchProduct(userChat.get().getCategory(), currentAttributeValues);
        }
        else if(intent.equals("add feature"))
        {
            searchStateService.addAttributes(message, chatId);
        }
        else if(intent.equals("buy product"))
        {
            searchStateService.buyProduct(message, chatId);
        }
        else if(intent.equals("add to cart"))
        {
            searchStateService.addToCartProduct(message, chatId);
        }
        else {
            throw new RuntimeException();
        }


        return chatEntityResponse;
    }




}
