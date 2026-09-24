package com.product.services;


import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;
import com.product.entity.Product;
import com.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private  final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        UpdateProductFromRequest(product,productRequest);
        Product savedproduct=productRepository.save(product);
        return  mapToProductResponse(savedproduct);
    }

    private ProductResponse mapToProductResponse(Product savedproduct) {
        ProductResponse response=new ProductResponse();
        response.setId(savedproduct.getId());
        response.setName(savedproduct.getName());
        response.setDescription(savedproduct.getDescription());
        response.setImagerurl(savedproduct.getImagerurl());
        response.setCategory(savedproduct.getCategory());
        response.setPrice(savedproduct.getPrice());
        response.setStockquantity(savedproduct.getStockquantity());
        return response;

    }


    private void UpdateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setImagerurl(productRequest.getImagerurl());
        product.setCategory(productRequest.getCategory());
        product.setPrice(productRequest.getPrice());
        product.setStockquantity(productRequest.getStockquantity());
        }

    public Optional<ProductResponse> updateProduct(long id, ProductRequest productRequest) {
      return  productRepository.findById(id).map(extestingproduct->{
             UpdateProductFromRequest(extestingproduct,productRequest);
             Product updatedProduct=productRepository.save(extestingproduct);
             return  mapToProductResponse(updatedProduct);
         });
    }


    public List<ProductResponse> getAllproduct() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public boolean deleteproduct(long id) {
        return productRepository.findById(id).map(product -> {
            product.setActive(false);
            productRepository.save(product);
            return true;
        }).orElse(false);
    }

    public List<ProductResponse> searchProduct(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }
}

