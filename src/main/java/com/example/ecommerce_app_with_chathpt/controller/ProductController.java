package com.example.ecommerce_app_with_chathpt.controller;

import com.example.ecommerce_app_with_chathpt.model.Product;
import com.example.ecommerce_app_with_chathpt.model.request.ProductRequest;
import com.example.ecommerce_app_with_chathpt.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;


    @GetMapping("")
    public List<Product> getAll(){
        return productService.getAll();

    }

    @PostMapping("")
    public ResponseEntity<Object> saveAllProducts(@RequestBody List<ProductRequest> productRequestList){
        return productService.saveAllProducts(productRequestList);
    }
}
