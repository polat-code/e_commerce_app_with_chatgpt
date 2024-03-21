package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.AttributeValue;
import com.example.ecommerce_app_with_chathpt.model.ChatEntity;
import com.example.ecommerce_app_with_chathpt.util.mapper.GptAttributeAndAttributeValuesJsonResponseToMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SearchStateService {

    private ChatGPTService chatGPTService;



    public List<GptAttributeAndAttributeValuesJsonResponseToMapper> removeAttributes(String message, List<AttributeValue> attributeValues){


        String prompt = attributeValues.toString();
        String manipulatedPrompt =  "Given a user request and product features, identify which features the user wants to remove. " +
                "Return features to be removed with example format. If user request do not match any Product feature return empty string, return an empty string. " +
                "Product Of Feature :" +   prompt  +
                "Examples: " +
                "1) User Request: 'Remove Chinese products', " +
                "Features: [{" +
                "    \"key\": \"Item Weight\", \"values\": [\"23 pounds\"]" +
                "}, {" +
                "    \"key\": \"Country of Origin\", \"values\": [\"China\"]" +
                "}]" +
                "Response: [{\"key\": \"Country of Origin\", \"values\": [\"China\"]}]" +
                "2) User Request: 'Exclude products heavier than 20 pounds', " +
                "Features: [{" +
                "    \"key\": \"Item Weight\", \"values\": [\"23 pounds\", \"15 pounds\"]" +
                "}, {" +
                "    \"key\": \"Color\", \"values\": [\"Red\", \"Blue\"]" +
                "}]" +
                "Response: [{\"key\": \"Item Weight\", \"values\": [\"23 pounds\"]}]" +
                "3) User Request: 'I don't want red or blue items', " +
                "Features: [{" +
                "    \"key\": \"Color\", \"values\": [\"Red\", \"Blue\", \"Green\"]" +
                "}, {" +
                "    \"key\": \"Size\", \"values\": [\"Small\", \"Medium\"]" +
                "}]" +
                "Response: [{\"key\": \"Color\", \"values\": [\"Red\", \"Blue\"]}]";

        String attributeResponseFromChatGPT = chatGPTService.sendRequestToChatGPT(message, manipulatedPrompt);
        attributeResponseFromChatGPT = attributeResponseFromChatGPT.replace("Response: ", "");
        List<GptAttributeAndAttributeValuesJsonResponseToMapper> attributeValuesJsonResponseToMapperList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            attributeValuesJsonResponseToMapperList = objectMapper
                    .readValue(attributeResponseFromChatGPT,
                            new TypeReference<>() {
                            });
        } catch (IOException e) {
            e.getMessage();
        }


        return attributeValuesJsonResponseToMapperList;
    }



    public ChatEntity addAttributes(String message, String chatId){
        return new ChatEntity();
    }


    public ChatEntity buyProduct(String message, String chatId){
        return new ChatEntity();
    }


    public ChatEntity addToCartProduct(String message, String chatId){
        return new ChatEntity();
    }


}
