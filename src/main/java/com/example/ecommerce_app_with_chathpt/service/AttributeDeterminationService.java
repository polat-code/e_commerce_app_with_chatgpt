package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.Attribute;
import com.example.ecommerce_app_with_chathpt.model.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class AttributeDeterminationService {

    private AttributeService attributeService;
    private ChatGPTService chatGPTService;

    public List<Attribute> determineAttributes(Category category, String message)
    {
        List<Attribute> attributes = attributeService.getAllAttributesByCategory(category);
        List<String> attributeNames = attributes.stream().map(Attribute::getName).toList();
        List<String> attributeResponseFromGpt = getAttributeResponseFromChatGPT(message, attributeNames);
        System.out.println(attributeResponseFromGpt);
        List<Attribute> relatedAttributes = attributeService.getAttributeListByListOfAttributeName(category, attributeResponseFromGpt);

        return relatedAttributes;
    }

    private List<String> getAttributeResponseFromChatGPT(String message, List<String> attributeNames) {

        String manipulatedPrompt = "I have a user request and I have the feature names of a category." +
                " I need to extract the feature names of this category according to the user's request." +
                " Do not return to me features that are not requested by the user." +
                " You just have to return me the feature names not their details the user requested in the format I sent them.must return only array" +
                " If there are no features, send me an empty string." +
                "Notice that returned features are from my features." + " Features:" + attributeNames + "." +
                "For example, User Request: I want to buy for a Nail Care Products,Features:{" +
                "    [" +
                "        Item Weight, Country of Origin" +
                "    ]" +
                "Response: []" +
                "For example, User Request: I want to buy for a 23 pound and American Nail Care Products,Features:{" +
                "    [" +
                "        Item Weight, Country of Origin" +
                "    ]" +
                "}" +
                "Response:[ Item Weight, Country of Origin]";


        String attributeResponseFromChatGPT = chatGPTService.sendRequestToChatGPT(message, manipulatedPrompt);

        String cleanedResponse = attributeResponseFromChatGPT.substring(1, attributeResponseFromChatGPT.length() - 1).trim();

        String[] attributeArray = cleanedResponse.split(",\\s*\"?");

        List<String> attributes = Arrays.asList(attributeArray);
        return attributes;

    }
}
