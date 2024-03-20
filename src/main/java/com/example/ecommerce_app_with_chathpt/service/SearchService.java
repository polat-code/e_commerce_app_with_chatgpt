package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.*;
import com.example.ecommerce_app_with_chathpt.util.mapper.GptAttributeAndAttributeValuesJsonResponseToMapper;
import com.example.ecommerce_app_with_chathpt.util.mapper.SearchAttributeKeyValueJsonMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchService {

    private CategoryService categoryService;
    private AttributeValueService attributeValueService;
    private AttributeService attributeService;
    private ProductSearchService productSearchService;
    private ChatGPTService chatGPTService;

    public ChatEntity searchByRequest(String message) {
        Category category = determineCategory(message);

        List<Attribute> attributes = attributeService.getAllAttributesByCategory(category);
        Map<Attribute, List<AttributeValue>> possibleAttributeValues = getPossibleAttributeValues(attributes);
        List<SearchAttributeKeyValueJsonMapper> attributeKeyValueJsonMapperList = mapAttributesToJsonMapper(possibleAttributeValues);
        List<GptAttributeAndAttributeValuesJsonResponseToMapper> attributeResponse = getAttributeResponseFromChatGPT(message, attributeKeyValueJsonMapperList);
        return productSearchService.searchProduct(attributeResponse, category);
    }

    private Category determineCategory(String message) {
        List<String> allCategoriesByParent = categoryService.findByParentCategoryIsNull().stream()
                .map(category -> category.toString().replace("\"", "")) // Remove quotation marks
                .collect(Collectors.toList());
        Optional<Category> optionalCategory = Optional.empty();
        boolean flag = true;
        while (flag) {
            String manipulatedMessage = "These are categories of my system " + allCategoriesByParent +
                    ".according to user request, return just only the best suitable categoryName.Please do not send a sentence, must only be categoryName" +
                    "[{'input': 'I'm looking for smartphones.', 'output': Electronics}," +
                    "{'input': 'Show me running shoes.', 'output': Sportswear}," +
                    "{'input': 'I need a new bookshelf.', 'output': Furniture}," +
                    "{'input': 'Where can I find kitchen utensils?', 'output': Kitchen & Dining}," +
                    "{'input': 'I want to buy a dress.', 'output': Clothing}]";
            String categoryResponse = chatGPTService.sendRequestToChatGPT(message, manipulatedMessage);
            String fixedCategoryResponse = categoryResponse.replace("\"", "");

            if (!allCategoriesByParent.contains(fixedCategoryResponse)){
                optionalCategory = categoryService.getCategoryByCategoryNameAndParentCategoryName(fixedCategoryResponse, optionalCategory.get());
                break;
            }

            if (optionalCategory.isPresent()) {
                optionalCategory = categoryService.getCategoryByCategoryNameAndParentCategoryName(fixedCategoryResponse, optionalCategory.get());
            } else {
                optionalCategory = categoryService.getCategoryByCategoryName(fixedCategoryResponse);
            }

            if (optionalCategory.isPresent()) {
                allCategoriesByParent = categoryService.getCategoryByParentCategory(optionalCategory.get()).stream()
                        .map(category -> category.toString().replace("\"", "")) // Remove quotation marks
                        .toList();
                if (allCategoriesByParent.isEmpty()) {
                    flag = false;
                }
            } else {
                flag = false;
            }
        }

        if (optionalCategory.isEmpty()) {
            throw new RuntimeException("Category is not found ");
        }
        return optionalCategory.get();
    }

    private Map<Attribute, List<AttributeValue>> getPossibleAttributeValues(List<Attribute> attributes) {
        Map<Attribute, List<AttributeValue>> possibleAttributeValueList = new HashMap<>();
        for (Attribute attribute : attributes) {
            List<AttributeValue> attributeValueList = attributeValueService.getAllByAttribute(attribute);
            possibleAttributeValueList.put(attribute, attributeValueList);
        }
        return possibleAttributeValueList;
    }

    private List<SearchAttributeKeyValueJsonMapper> mapAttributesToJsonMapper(Map<Attribute, List<AttributeValue>> possibleAttributeValues) {
        List<SearchAttributeKeyValueJsonMapper> attributeKeyValueJsonMapperList = new ArrayList<>();
        for (Attribute key : possibleAttributeValues.keySet()) {
            List<AttributeValue> values = possibleAttributeValues.get(key);
            SearchAttributeKeyValueJsonMapper attributeKeyValueJsonMapper = new SearchAttributeKeyValueJsonMapper();
            attributeKeyValueJsonMapper.setKey(key.getName());
            attributeKeyValueJsonMapper.setValues(new ArrayList<>());
            for (AttributeValue value : values) {
                attributeKeyValueJsonMapper.getValues().add(value.getValue());
            }
            attributeKeyValueJsonMapperList.add(attributeKeyValueJsonMapper);
        }
        return attributeKeyValueJsonMapperList;
    }

    private List<GptAttributeAndAttributeValuesJsonResponseToMapper> getAttributeResponseFromChatGPT(String message, List<SearchAttributeKeyValueJsonMapper> attributeKeyValueJsonMapperList) {
        String prompt = attributeKeyValueJsonMapperList.toString();
        String manipulatedPrompt = "I have a user request and I have the features of a category." +
                " I need to extract the features of this category according to the user's request." +
                " Do not return to me features that are not requested by the user." +
                " You just have to return me the features the user requested in the format I sent them.must return only array" +
                " If there are no features, send me an empty string." +
                "Notice that returned features are from my features." + " Features:" + prompt + "." +
                "For example, User Request: I want to buy for a Nail Care Products,Features:{" +
                "    [" +
                "        {\"key\": \"Item Weight\", \"values\": [\"23 pounds\"]}," +
                "        {\"key\": \"Country of Origin\", \"values\": [\"China\"]}" +
                "    ]" +
                "}" +
                "Response: []" +
                "For example, User Request: I want to buy for a 23 pound and American Nail Care Products,Features:{" +
                "     [" +
                "        {\"key\": \"Item Weight\", \"values\": [\"23 pounds\"]}," +
                "        {\"key\": \"Country of Origin\", \"values\": [\"China\"]}" +
                "    ]" +
                "}" +
                "Response:  [{\"key\": \"Item Weight\", \"values\": [\"23 pounds\"]},\"]";
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
        return attributeValuesJsonResponseToMapperList.stream().map(x -> new GptAttributeAndAttributeValuesJsonResponseToMapper(x.getKey(), x.getValues())).toList();
    }
}
