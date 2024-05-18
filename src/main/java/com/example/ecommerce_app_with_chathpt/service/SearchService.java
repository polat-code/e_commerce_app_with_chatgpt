package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.*;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ChatResponse;
import com.example.ecommerce_app_with_chathpt.util.mapper.GptAttributeAndAttributeValuesJsonResponseToMapper;
import com.example.ecommerce_app_with_chathpt.util.mapper.SearchAttributeKeyValueJsonMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class SearchService {

    private ProductSearchService productSearchService;
    private UserChatService userChatService;
    private CategoryDeterminationService categoryDeterminationService;
    private AttributeDeterminationService attributeDeterminationService;
    private AttributeValueDeterminationService attributeValueDeterminationService;

    public ChatResponse searchByRequest(String message, String chatId) {

        Category category = categoryDeterminationService.determineCategory(message);

        List<Attribute> determinedAttributes = attributeDeterminationService.determineAttributes(category, message);

        List<AttributeValue> determinedAttributeValues = attributeValueDeterminationService.determineAttributeValues(determinedAttributes, message, category);

        userChatService.setAttributeValuesAndCategoryOfChat(chatId, determinedAttributeValues, category);
        //3.5 SEC
        if (determinedAttributeValues.isEmpty()){
            return productSearchService.searchProductWithCategory(category);
        }else {
            return productSearchService.searchProduct(category, determinedAttributeValues);
        }

    }



}
