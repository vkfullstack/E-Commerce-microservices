package com.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "products")
@Data
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;
    private  String name;
    private  String description;
    private  String imagerurl;
    private  String category;
    private BigDecimal price;
    private  Integer stockquantity;
    private Boolean active=true;
    @CreationTimestamp
    private LocalDateTime creatAt;
    @UpdateTimestamp
    private  LocalDateTime updateAt;

}
