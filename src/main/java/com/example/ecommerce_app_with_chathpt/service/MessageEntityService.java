package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.MessageEntity;
import com.example.ecommerce_app_with_chathpt.repository.MessageEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageEntityService {

    private MessageEntityRepository messageEntityRepository;

    public void addMessageEntity(MessageEntity messageEntity){
        messageEntityRepository.save(messageEntity);
    }

}
