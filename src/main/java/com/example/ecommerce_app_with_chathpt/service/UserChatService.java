package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.ChatEntity;
import com.example.ecommerce_app_with_chathpt.model.MessageEntity;
import com.example.ecommerce_app_with_chathpt.model.ProductListEntity;
import com.example.ecommerce_app_with_chathpt.model.UserChat;
import com.example.ecommerce_app_with_chathpt.model.dto.ProductResponse;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ProductListResponse;
import com.example.ecommerce_app_with_chathpt.model.enums.ChatState;
import com.example.ecommerce_app_with_chathpt.repository.ChatEntityRepository;
import com.example.ecommerce_app_with_chathpt.repository.MessageEntityRepository;
import com.example.ecommerce_app_with_chathpt.repository.UserChatRepository;
import com.example.ecommerce_app_with_chathpt.util.Prompts;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserChatService {


    private UserChatRepository userChatRepository;
    private BotService botService;
    private ChatEntityService chatEntityService;
    private MessageEntityService messageEntityService;


    public UserChat createChat() {
        ChatEntity chatEntity = chatEntityService.addChatEntity(MessageEntity.builder()
                .messageContent("Hi Welcome to Wise")
                .creationTime(Date.from(ZonedDateTime.now().toInstant())).build());
        List<ChatEntity> chatEntityList = new ArrayList<>();
        chatEntityList.add(chatEntity);
        UserChat userChat = UserChat.builder().chatState(ChatState.INITIAL)
                .chatRecord(chatEntityList).build();


        return userChatRepository.save(userChat);
    }

    public ResponseEntity<ProductListResponse>  sendMessage(String chatId, String message) throws JsonProcessingException {
        addMessageToChat(chatId,message);
        ChatState stateOfChat = getStateOfMessage(chatId);
        ChatEntity chatEntityResponse = new ChatEntity() ;
        String response= "";
        if (stateOfChat.equals(ChatState.INITIAL)){
            response = botService.intentExtraction(message);
            // TODO Create the structure of state management.
            // setStateOfChatForInitialState(chatId, response);
            chatEntityResponse = botService.intentDirector(response,message);

        } else if (stateOfChat.equals(ChatState.SEARCH)) {

        }
        else if (stateOfChat.equals(ChatState.LOGIN)) {

        }
        else if (stateOfChat.equals(ChatState.REGISTER)) {

        }

        addChatEntityToUserChat(chatId, chatEntityService.addChatEntity(chatEntityResponse));

        return new ResponseEntity<>(listEntityToResponseListEntity((ProductListEntity) chatEntityResponse), HttpStatus.OK);



    }

    private ProductListResponse listEntityToResponseListEntity(ProductListEntity productListEntity) {
        ProductListResponse productListResponse = ProductListResponse.builder()
                .creationTime(Date.from(ZonedDateTime.now().toInstant()))
                .searchProducts(productListEntity.getSearchProducts().stream().map((product -> ProductResponse.builder()
                                .title(product.getTitle())
                                .brand(product.getBrand())
                                .url(product.getThumbnailImage())
                                .inStock(product.isInStock())
                                .price(product.getPrice())
                                .productId(product.getId())
                                .category(product.getCategory())
                                .attributeValues(product.getAttributeValues())
                                .build()))
                        .collect(Collectors.toList()))
                .returnType("productList")
                .build();
        return productListResponse;
    }

    private void setStateOfChatForInitialState(String chatId, String response) {
        Optional<UserChat> userChat = userChatRepository.findById(chatId);
        if (response.equals("search")){
            userChat.get().setChatState(ChatState.SEARCH);
            userChatRepository.save(userChat.get());
        }

    }

    private ChatState getStateOfMessage(String chatId) {
        return userChatRepository.findById(chatId).get().getChatState();
    }


    private void addMessageToChat(String chatId, String message){

        ChatEntity chatEntity = MessageEntity.builder()
                .creationTime(Date.from(ZonedDateTime.now().toInstant()))
                .messageContent(message).build();

        ChatEntity chatEntityFromRepo = chatEntityService.addChatEntity(chatEntity);
        MessageEntity messageEntity = messageEntityService.addMessageEntity((MessageEntity) chatEntityFromRepo);
        addChatEntityToUserChat(chatId, messageEntity);


    }

    private void addChatEntityToUserChat(String chatId, ChatEntity chatEntity){
        Optional<UserChat> userChat = userChatRepository.findById(chatId);
        if (userChat.isPresent()){
            userChat.get().getChatRecord().add(chatEntity);
            userChatRepository.save(userChat.get());
        }
    }

    public List<UserChat> getAllChats() {
        return userChatRepository.findAll();
    }
}
