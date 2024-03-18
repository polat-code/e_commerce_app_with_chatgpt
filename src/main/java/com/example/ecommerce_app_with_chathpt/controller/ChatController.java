package com.example.ecommerce_app_with_chathpt.controller;

import com.example.ecommerce_app_with_chathpt.model.ChatEntity;
import com.example.ecommerce_app_with_chathpt.model.UserChat;
import com.example.ecommerce_app_with_chathpt.service.UserChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@AllArgsConstructor
public class ChatController {

    private UserChatService userChatService;


    @PostMapping("")
    public UserChat createChat() {
        return userChatService.createChat();
    }

    @GetMapping("/{chatId}")
    public ChatEntity sendMessage(@PathVariable String chatId, @RequestBody String message) throws JsonProcessingException {
        return userChatService.sendMessage(chatId, message);
    }


}
