package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.dto.ProductRepository;
import com.example.ecommerce_app_with_chathpt.model.Attribute;
import com.example.ecommerce_app_with_chathpt.model.AttributeValue;
import com.example.ecommerce_app_with_chathpt.model.Category;
import com.example.ecommerce_app_with_chathpt.model.Product;
import com.example.ecommerce_app_with_chathpt.model.request.ProductRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;

    private CategoryService categoryService;

    private AttributeService attributeService;

    private AttributeValueService attributeValueService;


    public List<Product> getAll(){
        return productRepository.findAll();
    }


    public ResponseEntity<Object> saveAllProducts(List<ProductRequest> productRequestList) {


        for (ProductRequest productRequest : productRequestList) {
            List<AttributeValue> attributeValueList= new ArrayList<>();

            Optional<Category> foundCategory = categoryService.getAllByCategoryName(productRequest.getCategoryName());

            //List<Attribute> foundAttributeList = attributeService.findByCategory(foundCategory.get());


            for(int i = 0; i < productRequest.getAttributeRequests().size(); i++){
                String key = productRequest.getAttributeRequests().get(i).getKey();
                String keyValue = productRequest.getAttributeRequests().get(i).getValue();

                // TODO Exception for found category if it is empty

                Optional<Attribute> optionalAttribute = attributeService.findByCategoryAndName(foundCategory.get(),key);

                if(optionalAttribute.isPresent()){
                   Optional<AttributeValue> optionalAttributeValue = attributeValueService.findByAttributeAndValue(optionalAttribute.get(),keyValue);
                    if (optionalAttributeValue.isPresent()){
                        attributeValueList.add(optionalAttributeValue.get());
                    }
                    else {
                        AttributeValue attributeValue =  attributeValueService.createAndSaveAttributeValueObject();
                    }



                }
                else{
                    // yoksa

                }


            }
            Product product = Product.builder().
                    attributes(attributeValueList).
                    brand(productRequest.getBrand())
                    .url(productRequest.getUrl())
                    .title(productRequest.getTitle())
                    .breadCrumbs(productRequest.getBreadCrumbs())
                    .thumbnailImage(productRequest.getThumbnailImage())
                    .price(productRequest.getPrice())
                    .inStock(productRequest.getInStock())
                    .build();


            productRepository.save(product);
        }

        return ResponseEntity.ok("Product is added");

    }
}
