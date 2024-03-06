package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.Attribute;
import com.example.ecommerce_app_with_chathpt.model.AttributeValue;
import com.example.ecommerce_app_with_chathpt.model.Category;
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

    public ResponseEntity<Object> searchByRequest(String message) {


        List<String> getAllCategoriesByParent = categoryService.findByParentCategoryIsNull().stream()
                .map(Category::toString)
                .collect(Collectors.toList());
        Optional<Category> optionalCategory =Optional.empty();

        //TODO Eğer bi alt seviyesinde ilişkili kategori bulamazsa o kategori kalamsını sağlayan bir yapı oluştur
        boolean flag=true;
        while(flag) {
            String manipulatedMessage ="These are categories of my system "+ getAllCategoriesByParent+".according to user request, return just only the best suitable categoryName.Please do not send a sentence, must only be categoryName"+
                    "[{'input': 'I'm looking for smartphones.', 'output': Electronics},"
                    + "{'input': 'Show me running shoes.', 'output': Sportswear},"
                    + "{'input': 'I need a new bookshelf.', 'output': Furniture},"
                    + "{'input': 'Where can I find kitchen utensils?', 'output': Kitchen & Dining},"
                    + "{'input': 'I want to buy a dress.', 'output': Clothing}]";
            String categoryResponse = chatGPTService.sendRequestToChatGPT(message,manipulatedMessage);
            categoryResponse= categoryResponse.replace("\"","");

            if (optionalCategory.isPresent()){
                optionalCategory = categoryService.getCategoryByCategoryNameAndParentCategoryName(categoryResponse,optionalCategory.get());

            }
            else {
                optionalCategory = categoryService.getCategoryByCategoryName(categoryResponse);
            }

            if (optionalCategory.isPresent()) {
                getAllCategoriesByParent = categoryService.getCategoryByParentCategory(optionalCategory.get()).stream()
                        .map(Category::toString)
                        .toList();
                if(getAllCategoriesByParent.isEmpty()) {
                    flag = false;

                }
            }
            else {
                flag=false;
            }

        }

        if(optionalCategory.isEmpty())
        {
            throw new RuntimeException("Category is not found ");
        }
        // TODO Category checks
        Category category = optionalCategory.get();


        List<Attribute> attributeList = attributeService.getAllAttributesByCategory(category);

        Map<Attribute,List<AttributeValue>> possibleAttributeValueList = new HashMap<>();

        for (Attribute attribute : attributeList) {
            List<AttributeValue> attributeValueList = attributeValueService.getAllByAttribute(attribute);

            possibleAttributeValueList.put(attribute, attributeValueList);

        }
        List<SearchAttributeKeyValueJsonMapper> attributeKeyValueJsonMapperList = new ArrayList<>();
        for (Attribute key : possibleAttributeValueList.keySet()) {
            List<AttributeValue> values = possibleAttributeValueList.get(key);

            SearchAttributeKeyValueJsonMapper attributeKeyValueJsonMapper = new SearchAttributeKeyValueJsonMapper();
            attributeKeyValueJsonMapper.setKey(key.getName());

            attributeKeyValueJsonMapper.setValues(new ArrayList<>());

            for (AttributeValue value : values) {
                attributeKeyValueJsonMapper.getValues().add(value.getValue());
            }
            attributeKeyValueJsonMapperList.add(attributeKeyValueJsonMapper);


        }

        String prompt = attributeKeyValueJsonMapperList.toString();

        String manipulatedPrompt = "I have a user request and I have the features of a category." +
                " I need to extract the features of this category according to the user's request." +
                " Do not return to me features that are not requested by the user." +
                " You just have to return me the features the user requested in the format I sent them.must return only array" +
                " If there are no features, send me an empty string." +
                "Notice that returned features are from my features." +" Features:"+ prompt + "."
                +"For example, User Request: I want to buy for a Nail Care Products,Features:{" +
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
                "Response:  [{\"key\": \"Item Weight\", \"values\": [\"23 pounds\"]},\"]";;

        String attributeResponseFromChatGPT = chatGPTService.sendRequestToChatGPT(message,manipulatedPrompt);
        attributeResponseFromChatGPT = attributeResponseFromChatGPT.replace("Response: ", "");
        System.out.println(attributeResponseFromChatGPT);


        List<GptAttributeAndAttributeValuesJsonResponseToMapper> attributeValuesJsonResponseToMapperList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            attributeValuesJsonResponseToMapperList = objectMapper
                    .readValue(attributeResponseFromChatGPT,
                            new TypeReference<List<GptAttributeAndAttributeValuesJsonResponseToMapper>>(){});


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        List<GptAttributeAndAttributeValuesJsonResponseToMapper> attributeResponse =  attributeValuesJsonResponseToMapperList.stream().map(x -> new GptAttributeAndAttributeValuesJsonResponseToMapper(x.getKey(),x.getValues())).toList();

        productSearchService.searchProduct(attributeResponse, category);














        return new ResponseEntity<>(prompt, HttpStatus.OK);







    }
}
