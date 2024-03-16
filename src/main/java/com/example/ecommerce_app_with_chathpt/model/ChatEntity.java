    package com.example.ecommerce_app_with_chathpt.model;

    import com.example.ecommerce_app_with_chathpt.model.abstracts.IChatEntity;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.experimental.SuperBuilder;
    import org.springframework.data.annotation.Id;
    import org.springframework.data.mongodb.core.mapping.Document;
    import org.springframework.data.mongodb.core.mapping.Field;
    import org.springframework.data.mongodb.core.mapping.FieldType;

    import java.util.Date;

    @Document("chat_entities")
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public class ChatEntity implements IChatEntity {
        @Id
        private String id;
        @Field(targetType = FieldType.DATE_TIME)
        private Date creationTime;
    }
