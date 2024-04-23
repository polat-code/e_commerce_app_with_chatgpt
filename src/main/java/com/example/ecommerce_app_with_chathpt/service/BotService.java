package com.example.ecommerce_app_with_chathpt.service;


import com.example.ecommerce_app_with_chathpt.config.OpenAIConfig;

import com.example.ecommerce_app_with_chathpt.model.*;
import com.example.ecommerce_app_with_chathpt.model.dto.request.ChatGPTRequest;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ChatGPTResponse;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ChatResponse;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ProductResponse;
import com.example.ecommerce_app_with_chathpt.model.enums.MessageType;
import com.example.ecommerce_app_with_chathpt.util.mapper.BuyProductResponseToMapper;
import com.example.ecommerce_app_with_chathpt.util.mapper.GptAttributeAndAttributeValuesJsonResponseToMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class BotService {

    private RestTemplate restTemplate;

    private OpenAIConfig openAIConfig;


    private SearchService searchService;

    private SearchStateService searchStateService;

    private ProductSearchService productSearchService;

    private UserChatService userChatService;

    private UserCartService userCartService;

    private ProductService productService;

    //TODO Give more examples to prompts

    public String intentExtractionForInitialState(String message) throws JsonProcessingException {
        String manipulatedMessage = "These are intents of my sentences = [search, login, register, show cart, other]. If the intent of the message I give you is one of these, return just the intent of that sentence."
                + "\nExample: 'I want to search for books.' Output: search"
                + "\nExample: 'How do I log in to my account?' Output: login"
                + "\nExample: 'I need to register for a new account.' Output: register"
                + "\nExample: 'Tell me more about your services.' Output: other"
                + "\nExample: 'Show my cart'";

        ChatGPTRequest request = new ChatGPTRequest(openAIConfig.getOpenai_model(),message,manipulatedMessage);
        ChatGPTResponse chatGPTResponse = restTemplate.postForObject(openAIConfig.getOpenai_api_url(), request, ChatGPTResponse.class);
        String intent = chatGPTResponse.getChoices().get(0).getMessage().getContent();
        return intent;
    }

    public String intentExtractionForSearchState(String message) throws JsonProcessingException {
        String manipulatedMessage = "These are intents of my sentences = [search, remove feature, add feature, buy product, add to cart, show cart, exit]. If the intent of the message I give you is one of these, return just the intent of that sentence."
                + "\nExample: 'I want to search for books.' Output: search"
                + "\nExample: 'I also want to look for red products.' Output: add feature"
                + "\nExample: 'Do not show products with GTX2060' Output: remove feature"
                + "\nExample: 'I want to add fourth product to my cart' Output: remove feature"
                + "\nExample: 'I want to buy second product.' Output: buy product"
                + "\nExample: 'I want to exit.' Output: exit";


        ChatGPTRequest request = new ChatGPTRequest(openAIConfig.getOpenai_model(),message,manipulatedMessage);
        ChatGPTResponse chatGPTResponse = restTemplate.postForObject(openAIConfig.getOpenai_api_url(), request, ChatGPTResponse.class);
        String intent = chatGPTResponse.getChoices().get(0).getMessage().getContent();
        return intent;
    }




    public ChatResponse intentDirectorInitialState(Principal connectedUser, String intent, String message, String chatId) throws JsonProcessingException {

        ChatResponse chatEntityResponse = new ChatResponse();

        if (intent.equals("login")){


        }
        else if(intent.equals("search"))
        {
            chatEntityResponse = searchService.searchByRequest(message, chatId);
            userChatService.setStateForSearch(chatId);
        }
        else if(intent.equals("register"))
        {

        }
        else if(intent.equals("show cart"))
        {
            UserCart userCart = userCartService.getByUserId(connectedUser).get();
            System.out.println(userCart.getCartProducts());
            userChatService.setStateForCart(chatId);

        }
        else {
            throw new RuntimeException();
        }


        return chatEntityResponse;
    }

    public ChatResponse intentDirectorSearchState(Principal connectedUser, String intent, String message, String chatId) throws JsonProcessingException {

        ChatResponse chatEntityResponse = new ChatResponse();
        if(intent.equals("search"))
        {
            chatEntityResponse = searchService.searchByRequest(message, chatId);

        }

        else if(intent.equals("remove feature"))
        {
            //TODO Should be removed but it did not remove
            Optional<UserChat> userChat = userChatService.getUserChatById(chatId);

            List<GptAttributeAndAttributeValuesJsonResponseToMapper> attributeValueList = searchStateService.removeAttributes(message, userChat.get().getAttributeValues());
            List<AttributeValue> currentAttributeValues = userChat.get().getAttributeValues();
            List<AttributeValue> featuresToBeRemoved = productSearchService.mapAttributeValues(attributeValueList, userChat.get().getCategory());
            currentAttributeValues.removeIf(featuresToBeRemoved::contains);

            userChatService.setAttributeValuesAndCategoryOfChat(chatId,currentAttributeValues,userChat.get().getCategory());
            return productSearchService.searchProduct(userChat.get().getCategory(), currentAttributeValues);
        }
        else if(intent.equals("add feature"))
        {
            //TODO Should be implemented

        }
        else if(intent.equals("buy product"))
        {

            //TODO buy product and add to cart should call same methods
            System.out.println(message);
            BuyProductResponseToMapper buyProductResponseToMapper = searchStateService.buyProduct(message);
            System.out.println(buyProductResponseToMapper);
            ProductResponse productResponse = userChatService.findProductWithIndex(buyProductResponseToMapper.getIndex(),connectedUser,chatId);

            Optional<Product> product = productService.getProductById(productResponse.getProductId());

            userCartService.addProductToUserCart(connectedUser, product.get(), buyProductResponseToMapper.getQuantity());
            

        }
        else if(intent.equals("add to cart"))
        {
            BuyProductResponseToMapper buyProductResponseToMapper = searchStateService.buyProduct(message);
            ProductResponse productResponse = userChatService.findProductWithIndex(buyProductResponseToMapper.getIndex(),connectedUser,chatId);

            Optional<Product> product = productService.getProductById(productResponse.getProductId());

            userCartService.addProductToUserCart(connectedUser, product.get(), buyProductResponseToMapper.getQuantity());



        }

        else if (intent.equals("exit")){

            //TODO Set state to initial state
            userChatService.setStateForInitial(chatId);

            return ChatResponse.builder().message("You are now in initial state")
                     .messageType(MessageType.chatMessage)
                     .build();

        }
        else {
            throw new RuntimeException();
        }




        return chatEntityResponse;
    }

    public ChatResponse intentDirectorCartState(Principal connectedUser, String intent, String message, String chatId) throws JsonProcessingException {

        ChatResponse chatEntityResponse = new ChatResponse();
        if(intent.equals("remove product from cart"))
        {

        }
        else if(intent.equals("increase product quantity"))
        {

        }
        else if(intent.equals("decrease product quantity"))
        {

            //TODO buy product and add to cart should call same methods
            System.out.println(message);
            BuyProductResponseToMapper buyProductResponseToMapper = searchStateService.buyProduct(message);
            System.out.println(buyProductResponseToMapper);
            ProductResponse productResponse = userChatService.findProductWithIndex(buyProductResponseToMapper.getIndex(),connectedUser,chatId);

            Optional<Product> product = productService.getProductById(productResponse.getProductId());

            userCartService.addProductToUserCart(connectedUser, product.get(), buyProductResponseToMapper.getQuantity());


        }
        else if(intent.equals("update product quantity"))
        {
            BuyProductResponseToMapper buyProductResponseToMapper = searchStateService.buyProduct(message);
            ProductResponse productResponse = userChatService.findProductWithIndex(buyProductResponseToMapper.getIndex(),connectedUser,chatId);

            Optional<Product> product = productService.getProductById(productResponse.getProductId());

            userCartService.addProductToUserCart(connectedUser, product.get(), buyProductResponseToMapper.getQuantity());



        }

        else if (intent.equals("exit")){

            //TODO Set state to initial state
            userChatService.setStateForInitial(chatId);
            return ChatResponse.builder().message("You are now in initial state")
                    .messageType(MessageType.chatMessage)
                    .build();

        }
        else {
            throw new RuntimeException();
        }




        return chatEntityResponse;
    }





}
