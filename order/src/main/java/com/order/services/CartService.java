package com.order.services;

import com.order.dto.CartItemRequest;
import com.order.entity.CartItem;
import com.order.repository.CartIteamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

   private final CartIteamRepository cartIteamRepository;

   public Boolean addToCart(String userId, CartItemRequest cartItemRequest) {
       if (cartItemRequest == null || cartItemRequest.getProductId() == null || cartItemRequest.getQuantity() == null || cartItemRequest.getQuantity() <= 0) {
           return false;
       }

       Long parsedUserId = Long.parseLong(userId);
       Long productId = cartItemRequest.getProductId();

       CartItem existingCartItem = cartIteamRepository.findByUserIdAndProductId(parsedUserId, productId);

       if (existingCartItem != null) {
           existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
           cartIteamRepository.save(existingCartItem);
           return true;
       }

       CartItem newCartItem = new CartItem();
       newCartItem.setUserId(parsedUserId);
       newCartItem.setProductId(productId);
       newCartItem.setQuantity(cartItemRequest.getQuantity());
       newCartItem.setPrice(BigDecimal.ZERO);
       cartIteamRepository.save(newCartItem);

       return true;
   }

   @Transactional
   public boolean deleteItemFromCart(String userId, String productId) {
       Long parsedUserId = Long.parseLong(userId);
       Long parsedProductId = Long.parseLong(productId);

       CartItem cartItem = cartIteamRepository.findByUserIdAndProductId(parsedUserId, parsedProductId);
       if (cartItem == null) {
           return false;
       }

       cartIteamRepository.delete(cartItem);
       return true;
   }

   public List<CartItem> getCartItems(String userId) {
       return cartIteamRepository.findAllByUserId(Long.parseLong(userId));
   }

   @Transactional
   public void clearCart(String userId) {
       cartIteamRepository.deleteAllByUserId(Long.parseLong(userId));
   }
}