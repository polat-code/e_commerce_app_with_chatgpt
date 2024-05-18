package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.Attribute;
import com.example.ecommerce_app_with_chathpt.model.AttributeValue;
import com.example.ecommerce_app_with_chathpt.model.Category;
import com.example.ecommerce_app_with_chathpt.util.mapper.GptAttributeAndAttributeValuesJsonResponseToMapper;
import com.example.ecommerce_app_with_chathpt.util.mapper.SearchAttributeKeyValueJsonMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class AttributeValueDeterminationService {

    private ChatGPTService chatGPTService;
    private AttributeService attributeService;
    private AttributeValueService attributeValueService;

    public List<AttributeValue> determineAttributeValues(List<Attribute> determinedAttributes, String message, Category category) {

        Map<Attribute, List<AttributeValue>> possibleAttributeValues = getPossibleAttributeValues(determinedAttributes);
        List<SearchAttributeKeyValueJsonMapper> attributeKeyValueJsonMapperList = mapAttributesToJsonMapper(possibleAttributeValues);
        List<GptAttributeAndAttributeValuesJsonResponseToMapper> attributeResponse = getAttributeValueResponseFromChatGPT(message, attributeKeyValueJsonMapperList);
        return mapAttributeValues(attributeResponse, category);
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

    private List<GptAttributeAndAttributeValuesJsonResponseToMapper> getAttributeValueResponseFromChatGPT(String message, List<SearchAttributeKeyValueJsonMapper> attributeKeyValueJsonMapperList) {
        List<SearchAttributeKeyValueJsonMapper> prompt = attributeKeyValueJsonMapperList.subList(0, Math.min(15, attributeKeyValueJsonMapperList.size()));


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

    public List<AttributeValue> mapAttributeValues(List<GptAttributeAndAttributeValuesJsonResponseToMapper> gptAttributeAndAttributeValuesJsonResponseToMappers,Category category){
        List<AttributeValue> attributeValues = new ArrayList<>();
        for (GptAttributeAndAttributeValuesJsonResponseToMapper gptAttributeAndAttributeValuesJsonResponseToMapper : gptAttributeAndAttributeValuesJsonResponseToMappers ){
            String key = gptAttributeAndAttributeValuesJsonResponseToMapper.getKey();

            List<String> values = gptAttributeAndAttributeValuesJsonResponseToMapper.getValues();
            for (String value: values){
                Optional<Attribute> optionalAttribute = attributeService.findByCategoryAndName(category, key);
                if (optionalAttribute.isPresent()) {
                    Optional<AttributeValue> attributeValue = attributeValueService.findByAttributeAndValue(optionalAttribute.get(),value);
                    attributeValue.ifPresent(attributeValues::add);
                }
            }
        }
        return attributeValues;
    }

}
