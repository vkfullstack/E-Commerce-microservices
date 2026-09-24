package com.product.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    private  long id;
    private  String name;
    private  String description;
    private  String imagerurl;
    private  String category;
    private BigDecimal price;
    private  Integer stockquantity;
    private Boolean active;
}
