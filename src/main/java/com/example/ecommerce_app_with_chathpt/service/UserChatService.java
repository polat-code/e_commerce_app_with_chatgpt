package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.*;
import com.example.ecommerce_app_with_chathpt.model.enums.ChatState;
import com.example.ecommerce_app_with_chathpt.repository.UserChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserChatService {


    private UserChatRepository userChatRepository;
    private ChatEntityService chatEntityService;
    private UserService userService;


    public UserChat createChat(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        ChatEntity chatEntity = chatEntityService.addChatEntity(MessageEntity.builder()
                .messageContent("Hi Welcome to Wise")

                .creationTime(Date.from(ZonedDateTime.now().toInstant())).build());
        List<ChatEntity> chatEntityList = new ArrayList<>();
        chatEntityList.add(chatEntity);
        List<AttributeValue> attributeValues = new ArrayList<>();
        Optional<User> optionalUser = userService.findById(user.getId());
        UserChat userChat = UserChat.builder().chatState(ChatState.INITIAL)
                .attributeValues(attributeValues)
                .user(optionalUser.get())
                .chatRecord(chatEntityList).build();


        return userChatRepository.save(userChat);
    }

    public void setAttributeValuesAndCategoryOfChat(String chatId, List<AttributeValue> attributeValues, Category category) {
        Optional<UserChat> optUserChat = userChatRepository.findById(chatId);

        if (optUserChat.isPresent()){
            UserChat userChat = optUserChat.get();
            userChat.setAttributeValues(attributeValues);
            userChat.setCategory(category);
            userChatRepository.save(userChat);

        }
    }

    public List<AttributeValue> getChatAttributesById(String chatId) {
        return userChatRepository.findAttributeValuesByChatId(chatId);
    }

    public Optional<Category> getCategoryOfChat(String chatId) {
        return userChatRepository.findChatCategoryByChatId(chatId);
    }

    public Optional<UserChat> getUserChatById(String chatId) {
        return userChatRepository.findById(chatId);
    }


    public List<UserChat> getAllChatsByConnectedUser(Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return userChatRepository.findAllByUser_Id(user.getId());
    }
}
