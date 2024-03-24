package com.example.ecommerce_app_with_chathpt.controller;

import com.example.ecommerce_app_with_chathpt.model.ChatEntity;
import com.example.ecommerce_app_with_chathpt.model.User;
import com.example.ecommerce_app_with_chathpt.model.UserChat;
import com.example.ecommerce_app_with_chathpt.model.dto.request.SendMessageChatRequest;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ChatResponse;
import com.example.ecommerce_app_with_chathpt.service.MessageService;
import com.example.ecommerce_app_with_chathpt.service.UserChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
@AllArgsConstructor
@CrossOrigin("http://localhost:3000")
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
    public ResponseEntity<ChatResponse>  sendMessage(@RequestBody SendMessageChatRequest sendMessageChatRequest) throws JsonProcessingException {

        log.info(sendMessageChatRequest.getMessage());
        log.info(sendMessageChatRequest.getChatId());
        ChatResponse chatEntity = messageService.sendMessage(sendMessageChatRequest.getChatId(), sendMessageChatRequest.getMessage());
        System.out.println(chatEntity);
        return new ResponseEntity<>(chatEntity,HttpStatus.OK);
    }

/*


    @PostMapping("/send-message")
    public ChatEntity sendMessageWithPrompt(@RequestBody SendMessageChatRequest sendMessageChatRequest) throws JsonProcessingException{
        log.info(sendMessageChatRequest.getMessage());
        log.info(sendMessageChatRequest.getChatId());
        return messageService.sendMessage(sendMessageChatRequest.getChatId(), sendMessageChatRequest.getMessage());
    }   */

}
