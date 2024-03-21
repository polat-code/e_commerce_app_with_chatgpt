package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.AttributeValue;
import com.example.ecommerce_app_with_chathpt.model.ChatEntity;
import com.example.ecommerce_app_with_chathpt.model.MessageEntity;
import com.example.ecommerce_app_with_chathpt.model.UserChat;
import com.example.ecommerce_app_with_chathpt.model.enums.ChatState;
import com.example.ecommerce_app_with_chathpt.repository.UserChatRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageService {

    private UserChatRepository userChatRepository;
    private BotService botService;
    private ChatEntityService chatEntityService;
    private MessageEntityService messageEntityService;


    public ChatEntity sendMessage(String chatId, String message) throws JsonProcessingException {
        addMessageToChat(chatId,message);
        ChatState stateOfChat = getStateOfMessage(chatId);
        ChatEntity chatEntityResponse = new ChatEntity() ;
        String response= "";
        if (stateOfChat.equals(ChatState.INITIAL)){
            response = botService.intentExtractionForInitialState(message);
            // TODO Create the structure of state management.
            setStateOfChatForInitialState(chatId, response);
            chatEntityResponse = botService.intentDirectorInitialState(response,message, chatId);

        } else if (stateOfChat.equals(ChatState.SEARCH)) {
            response = botService.intentExtractionForSearchState(message);
            // TODO Create the structure of state management.
            // setStateOfChatForInitialState(chatId, response);
            chatEntityResponse = botService.intentDirectorSearchState(response,message, chatId);

        }
        else if (stateOfChat.equals(ChatState.LOGIN)) {

        }
        else if (stateOfChat.equals(ChatState.REGISTER)) {

        }

        //addChatEntityToUserChat(chatId, chatEntityService.addChatEntity(chatEntityResponse));

        return chatEntityResponse;



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


}
