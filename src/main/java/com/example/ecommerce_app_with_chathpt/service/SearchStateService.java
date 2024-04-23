package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.AttributeValue;
import com.example.ecommerce_app_with_chathpt.util.mapper.BuyProductResponseToMapper;
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




    public BuyProductResponseToMapper buyProduct(String message){

        String prompt = "Given a user request for products, extract the product index and quantity. " +
                "Assume a default quantity of 1 if not specified.\n" +
                "Examples:\n" +
                "1. Request: \"I want to buy product 3.\"\n" +
                "   Output: {\"index\": 3, \"quantity\": 1}\n" +
                "2. Request: \"Add 2 units of second product to my cart.\"\n" +
                "   Output: {\"index\": 2, \"quantity\": 2}\n" +
                "3. Request: \"I'd like to purchase product 1.\"\n" +
                "   Output: {\"index\": 1, \"quantity\": 1}\n" +
                "4. Request: \"Can I get 4 of product 9?\"\n" +
                "   Output: {\"index\": 9, \"quantity\": 4}";


        String buyProductResponseFromGpt = chatGPTService.sendRequestToChatGPT(message, prompt);
        BuyProductResponseToMapper buyProductResponseToMapper = new BuyProductResponseToMapper();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            buyProductResponseToMapper = objectMapper
                    .readValue(buyProductResponseFromGpt,
                            new TypeReference<>() {
                            });
        } catch (IOException e) {
            e.getMessage();
        }

        return buyProductResponseToMapper;
    }


    public BuyProductResponseToMapper addToCartProduct(String message
    ){
        String prompt = "Given a user request for products, extract the product index and quantity. " +
                "Assume a default quantity of 1 if not specified.\n" +
                "Examples:\n" +
                "1. Request: \"I want to buy product 3.\"\n" +
                "   Output: {\"index\": 3, \"quantity\": 1}\n" +
                "2. Request: \"Add 2 units of second product to my cart.\"\n" +
                "   Output: {\"index\": 2, \"quantity\": 2}\n" +
                "3. Request: \"I'd like to purchase product 1.\"\n" +
                "   Output: {\"index\": 1, \"quantity\": 1}\n" +
                "4. Request: \"Can I get 4 of product 9?\"\n" +
                "   Output: {\"index\": 9, \"quantity\": 4}";


        String buyProductResponseFromGpt = chatGPTService.sendRequestToChatGPT(message, prompt);
        BuyProductResponseToMapper buyProductResponseToMapper = new BuyProductResponseToMapper();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            buyProductResponseToMapper = objectMapper
                    .readValue(buyProductResponseFromGpt,
                            new TypeReference<>() {
                            });
        } catch (IOException e) {
            e.getMessage();
        }



        return buyProductResponseToMapper;
    }



}
