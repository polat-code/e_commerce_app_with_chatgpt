package com.example.ecommerce_app_with_chathpt.controller;

import com.example.ecommerce_app_with_chathpt.model.ChatEntity;
import com.example.ecommerce_app_with_chathpt.model.UserChat;
import com.example.ecommerce_app_with_chathpt.model.dto.request.SendMessageChatRequest;
import com.example.ecommerce_app_with_chathpt.service.UserChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@AllArgsConstructor
@CrossOrigin("http://localhost:3000")
@Slf4j
public class ChatController {

    private UserChatService userChatService;


    @PostMapping("")
    public UserChat createChat() {
        return userChatService.createChat();
    }

    @PostMapping("/chat")
    public ChatEntity sendMessage(@RequestBody SendMessageChatRequest sendMessageChatRequest) throws JsonProcessingException {
        log.info(sendMessageChatRequest.getMessage());
        log.info(sendMessageChatRequest.getChatId());
        return userChatService.sendMessage(sendMessageChatRequest.getChatId(), sendMessageChatRequest.getMessage());
    }

    @PostMapping("/send-message")
    public ChatEntity sendMessageWithPrompt(@RequestBody SendMessageChatRequest sendMessageChatRequest) throws JsonProcessingException{
        log.info(sendMessageChatRequest.getMessage());
        log.info(sendMessageChatRequest.getChatId());
        return userChatService.sendMessage(sendMessageChatRequest.getChatId(), sendMessageChatRequest.getMessage());
    }


}
