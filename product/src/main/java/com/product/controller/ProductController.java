package com.product.controller;


import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;
import com.product.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductRequest productRequest) {

        return new ResponseEntity<ProductResponse>(
                productService.createProduct(productRequest),
                HttpStatus.CREATED
        );
    }
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllproduct() {

        return ResponseEntity.ok(productService.getAllproduct()
        );
    }

        @PutMapping("/{id}")
        public ResponseEntity<ProductResponse> updateProduct(@PathVariable long id, @RequestBody ProductRequest productRequest) {

           return productService.updateProduct(id, productRequest)
                    .map( ResponseEntity::ok )
                    .orElseGet(() -> ResponseEntity.notFound().build()
            );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteproduct(@PathVariable  long id){
      boolean deleted=  productService.deleteproduct(id);
        return  deleted ? ResponseEntity.noContent().build():ResponseEntity.notFound().build();
    }
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchproduct(@RequestParam String keyword) {

        return ResponseEntity.ok(productService.searchProduct(keyword));
    }
}