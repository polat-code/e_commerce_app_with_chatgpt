package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.ChatEntity;
import com.example.ecommerce_app_with_chathpt.model.MessageEntity;
import com.example.ecommerce_app_with_chathpt.model.UserChat;
import com.example.ecommerce_app_with_chathpt.model.enums.ChatState;
import com.example.ecommerce_app_with_chathpt.repository.MessageEntityRepository;
import com.example.ecommerce_app_with_chathpt.repository.UserChatRepository;
import com.example.ecommerce_app_with_chathpt.util.Prompts;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    public List<ChatEntity> sendMessage(String chatId, String message) throws JsonProcessingException {
        addMessageToChat(chatId,message);
        ChatState stateOfChat = getStateOfMessage(chatId);
        List<ChatEntity> chatEntityResponse = new ArrayList<>();
        String response= "";
        if (stateOfChat.equals(ChatState.INITIAL)){
            response = botService.intentExtraction(message);
            setStateOfChatForInitialState(chatId, response);
            chatEntityResponse = botService.intentDirector(response,message);
        } else if (stateOfChat.equals(ChatState.SEARCH)) {

        }
        else if (stateOfChat.equals(ChatState.LOGIN)) {

        }
        else if (stateOfChat.equals(ChatState.REGISTER)) {

        }
        addMessageToChat(chatId, response);

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
        Optional<UserChat> userChat = userChatRepository.findById(chatId);
        ChatEntity chatEntity = MessageEntity.builder()
                .creationTime(Date.from(ZonedDateTime.now().toInstant()))
                .messageContent(message).build();

        ChatEntity chatEntityFromRepo = chatEntityService.addChatEntity(chatEntity);
        messageEntityService.addMessageEntity((MessageEntity) chatEntityFromRepo);
        if (userChat.isPresent()){
            userChat.get().getChatRecord().add(chatEntityFromRepo);
            userChatRepository.save(userChat.get());
        }


    }
}
