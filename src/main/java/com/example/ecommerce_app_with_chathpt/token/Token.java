package com.example.ecommerce_app_with_chathpt.token;

import com.example.ecommerce_app_with_chathpt.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Token {

    @Id
    public String id;

    @Field(targetType = FieldType.STRING)
    public String token;

    @Field
    public TokenType tokenType = TokenType.BEARER;

    public boolean revoked;

    public boolean expired;

    @DBRef
    public User user;
}