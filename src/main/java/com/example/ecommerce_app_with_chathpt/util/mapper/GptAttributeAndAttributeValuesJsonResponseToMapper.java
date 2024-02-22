package com.example.ecommerce_app_with_chathpt.util.mapper;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GptAttributeAndAttributeValuesJsonResponseToMapper {

    private String key;
    private List<String> values;
}
