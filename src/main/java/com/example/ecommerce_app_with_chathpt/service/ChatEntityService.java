package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.ChatEntity;
import com.example.ecommerce_app_with_chathpt.model.MessageEntity;
import com.example.ecommerce_app_with_chathpt.repository.ChatEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatEntityService {

    private MessageEntityService messageEntityService;
    private ChatEntityRepository chatEntityRepository;



    public ChatEntity addChatEntity(ChatEntity chatEntity){
        return chatEntityRepository.save(chatEntity);
    }


}
