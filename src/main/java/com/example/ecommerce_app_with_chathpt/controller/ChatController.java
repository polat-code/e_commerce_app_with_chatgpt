package com.example.ecommerce_app_with_chathpt.controller;

import com.example.ecommerce_app_with_chathpt.model.UserChat;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ChatRecordResponse;
import com.example.ecommerce_app_with_chathpt.model.dto.request.SendMessageChatRequest;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ChatResponse;
import com.example.ecommerce_app_with_chathpt.service.MessageService;
import com.example.ecommerce_app_with_chathpt.service.UserChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
@AllArgsConstructor
@Slf4j
public class ChatController {

    private UserChatService userChatService;
    private MessageService messageService;


    @PostMapping("")
    public ResponseEntity<UserChat> createChat(Principal connectedUser) {

        // Use the user details to create a chat
        UserChat userChat = userChatService.createChat(connectedUser);

        return new ResponseEntity<>(userChat, HttpStatus.OK);
    }


    @GetMapping("/all")
    public ResponseEntity<List<UserChat>> getAllChats(Principal connectedUser) {
        List<UserChat> userChatList = userChatService.getAllChatsByConnectedUser(connectedUser);
        return new ResponseEntity<>(userChatList,HttpStatus.OK) ;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse>  sendMessage(Principal connectedUser, @RequestBody SendMessageChatRequest sendMessageChatRequest) throws JsonProcessingException {
        System.out.println(sendMessageChatRequest);
        log.info(sendMessageChatRequest.getMessage());
        log.info(sendMessageChatRequest.getChatId());
        ChatResponse chatResponse = messageService.sendMessage(connectedUser,sendMessageChatRequest.getChatId(), sendMessageChatRequest.getMessage());

        return new ResponseEntity<>(chatResponse,HttpStatus.OK);
    }


    @GetMapping("/chat/{chatId}")
    public ResponseEntity<ChatRecordResponse> getChatRecord(Principal connectedUser, @PathVariable String chatId){
        System.out.println(chatId);
        return userChatService.getChatRecords(connectedUser, chatId);
    }



/*


    @PostMapping("/send-message")
    public ChatEntity sendMessageWithPrompt(@RequestBody SendMessageChatRequest sendMessageChatRequest) throws JsonProcessingException{
        log.info(sendMessageChatRequest.getMessage());
        log.info(sendMessageChatRequest.getChatId());
        return messageService.sendMessage(sendMessageChatRequest.getChatId(), sendMessageChatRequest.getMessage());
    }   */

}
