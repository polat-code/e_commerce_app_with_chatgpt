package com.example.ecommerce_app_with_chathpt.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

@EqualsAndHashCode(callSuper = true)
@TypeAlias("messages")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageEntity extends ChatEntity{

    private String messageContent;

}
