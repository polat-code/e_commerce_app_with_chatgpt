package com.example.ecommerce_app_with_chathpt.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "user")
public class User {

    @Id
    private String id;


    @Field(name = "name")
    private String name;

    @Field(name = "surname")
    private String surname;

    @Field(name="phone")
    private String phone;

    @Field(name = "email")
    private String email;


    @Field(name = "password")
    private String password;


    /*
    * True = Activated
    * False = Inactivated
    * */
    @Field(name = "verified")
    @Builder.Default
    private boolean verified = false;








}
