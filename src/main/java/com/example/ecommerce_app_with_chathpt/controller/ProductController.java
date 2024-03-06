package com.example.ecommerce_app_with_chathpt.controller;

import com.example.ecommerce_app_with_chathpt.model.Product;
import com.example.ecommerce_app_with_chathpt.model.dto.request.ProductRequest;
import com.example.ecommerce_app_with_chathpt.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}")
    private Optional<Product> getProductById(@PathVariable String id){
        return productService.getProductById(id);
    }

}
