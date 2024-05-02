package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.*;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ChatRecordResponse;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ChatResponse;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ProductResponse;
import com.example.ecommerce_app_with_chathpt.model.enums.ChatState;
import com.example.ecommerce_app_with_chathpt.model.enums.MessageType;
import com.example.ecommerce_app_with_chathpt.repository.UserChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserChatService {


    private UserChatRepository userChatRepository;
    private UserService userService;


    public UserChat createChat(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        ChatResponse chatResponse = ChatResponse.builder()
                .messageType(MessageType.botMessage)
                .build();


        List<ChatResponse> chatResponseList = new ArrayList<>();
        chatResponseList.add(chatResponse);
        List<AttributeValue> attributeValues = new ArrayList<>();
        Optional<User> optionalUser = userService.findById(user.getId());
        UserChat userChat = UserChat.builder().chatState(ChatState.INITIAL)
                .attributeValues(attributeValues)
                .user(optionalUser.get())
                .chatRecord(chatResponseList).build();


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

    public void addChatResponseToChat(Principal connectedUser, String chatId, ChatResponse chatResponse){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Optional<UserChat> optionalUserChat = userChatRepository.findUserChatByIdAndUser_Id(chatId,user.getId());
        if (optionalUserChat.isPresent()){
            optionalUserChat.get().getChatRecord().add(chatResponse);
            userChatRepository.save(optionalUserChat.get());
        }
    }

    public ResponseEntity<ChatRecordResponse> getChatRecords(Principal connectedUser, String chatId) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Optional<UserChat> userChat = userChatRepository.findUserChatByIdAndUser_Id(chatId,user.getId());

        if (userChat.isPresent()){
            ChatRecordResponse chatRecordResponse = ChatRecordResponse.builder()
                    .chatResponses(userChat.get().getChatRecord())
                    .build();
            return new ResponseEntity<>(chatRecordResponse,HttpStatus.OK) ;
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    public ProductResponse findProductWithIndex(int index, Principal connectedUser, String chatId){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Optional<UserChat> userChat = userChatRepository.findUserChatByIdAndUser_Id(chatId, user.getId());
        if (userChat.isPresent()){
            List<ChatResponse> chatResponse = userChat.get().getChatRecord();
            List<ChatResponse> productChatResponse = chatResponse.stream()
                    .filter(cr -> cr.getMessageType().equals(MessageType.productList))
                    .toList();
            return productChatResponse.get(productChatResponse.size()-1).getProductList().get(index);
        }
        return  null;
    }

    public void setStateForSearch(String chatId) {
        UserChat userChat = userChatRepository.findById(chatId).get();
        userChat.setChatState(ChatState.SEARCH);
        userChatRepository.save(userChat);
    }

    public void setStateForInitial(String chatId) {
        UserChat userChat = userChatRepository.findById(chatId).get();
        userChat.setChatState(ChatState.INITIAL);
        userChatRepository.save(userChat);
    }

    public void setStateForCart(String chatId) {
        UserChat userChat = userChatRepository.findById(chatId).get();
        userChat.setChatState(ChatState.CART);
        userChatRepository.save(userChat);
    }
}
