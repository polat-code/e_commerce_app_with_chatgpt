package com.example.elasticchat.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@Builder
@Document(indexName = "attribute")
public class Attribute {

    @Id
    private String id;

    @Field(type = FieldType.Text, name = "name")
    private String name;


}