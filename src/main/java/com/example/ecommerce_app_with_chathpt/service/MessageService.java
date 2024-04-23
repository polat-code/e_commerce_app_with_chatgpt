package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.UserChat;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ChatResponse;
import com.example.ecommerce_app_with_chathpt.model.enums.ChatState;
import com.example.ecommerce_app_with_chathpt.model.enums.MessageType;
import com.example.ecommerce_app_with_chathpt.repository.UserChatRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageService {

    private UserChatRepository userChatRepository;
    private BotService botService;
    private UserChatService userChatService;



    public ChatResponse sendMessage(Principal connectedUser, String chatId, String message) throws JsonProcessingException {
        addMessageToChat(chatId,message);
        ChatState stateOfChat = getStateOfMessage(chatId);
        System.out.println(message);
        ChatResponse chatEntityResponse = new ChatResponse() ;
        String response= "";
        if (stateOfChat.equals(ChatState.INITIAL)){
            response = botService.intentExtractionForInitialState(message);
            // TODO Create the structure of state management.
            //setStateOfChatForInitialState(chatId, response);
            chatEntityResponse = botService.intentDirectorInitialState(connectedUser, response, message, chatId);

        } else if (stateOfChat.equals(ChatState.SEARCH)) {
            response = botService.intentExtractionForSearchState(message);
            // TODO Create the structure of state management.
            // setStateOfChatForInitialState(chatId, response);
            chatEntityResponse = botService.intentDirectorSearchState(connectedUser,response,message, chatId);

        }
        else if (stateOfChat.equals(ChatState.LOGIN)) {

        }
        else if (stateOfChat.equals(ChatState.REGISTER)) {

        }

        //addChatEntityToUserChat(chatId, chatEntityService.addChatEntity(chatEntityResponse));
        userChatService.addChatResponseToChat(connectedUser, chatId, chatEntityResponse);
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

        ChatResponse chatResponse = ChatResponse.builder()
                .messageType(MessageType.chatMessage)
                .message(message)
                .build();
        addChatEntityToUserChat(chatId, chatResponse);


    }

    private void addChatEntityToUserChat(String chatId, ChatResponse chatResponse){
        Optional<UserChat> userChat = userChatRepository.findById(chatId);
        if (userChat.isPresent()){
            userChat.get().getChatRecord().add(chatResponse);
            userChatRepository.save(userChat.get());
        }
    }


}
