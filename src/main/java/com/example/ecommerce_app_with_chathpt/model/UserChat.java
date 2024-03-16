package com.example.ecommerce_app_with_chathpt.model;

import com.example.ecommerce_app_with_chathpt.model.enums.ChatState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_chats")
public class UserChat {



    @Id
    private String id;

    @DBRef
    private String user_id;


    @DBRef
    private List<ChatEntity> chatRecord;

    @Field
    private ChatState chatState;


}
