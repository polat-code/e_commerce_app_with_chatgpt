package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.CartProduct;
import com.example.ecommerce_app_with_chathpt.model.Product;
import com.example.ecommerce_app_with_chathpt.model.User;
import com.example.ecommerce_app_with_chathpt.model.UserCart;
import com.example.ecommerce_app_with_chathpt.model.dto.response.CartProductResponse;
import com.example.ecommerce_app_with_chathpt.model.dto.response.CartResponse;
import com.example.ecommerce_app_with_chathpt.repository.UserCartRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class UserCartService {

    private UserCartRepository userCartRepository;


    public void addProductToUserCart(Principal connectedUser, Product product, int quantity) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        UserCart userCart = userCartRepository.findUserCartByUser_Id(user.getId())
                .orElseGet(() -> {
                    UserCart newUserCart = new UserCart();
                    newUserCart.setCartProducts(new ArrayList<>());
                    newUserCart.setTotalPrice(0.0);
                    newUserCart.setUser(user);
                    return newUserCart;
                });

        // Check if the product already exists in the cart
        Optional<CartProduct> existingProduct = userCart.getCartProducts().stream()
                .filter(p -> p.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingProduct.isPresent()) {
            // Product exists in the cart, just update the quantity
            CartProduct cartProduct = existingProduct.get();
            cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
        } else {
            // Product does not exist, add new CartProduct
            CartProduct newCartProduct = CartProduct.builder()
                    .product(product)
                    .quantity(quantity)
                    .creationTime(Date.from(Instant.now()))
                    .build();
            userCart.getCartProducts().add(newCartProduct);
        }

        // Recalculate total price
        double totalPrice = userCart.getCartProducts().stream()
                .mapToDouble(cp -> cp.getProduct().getPrice() * cp.getQuantity())
                .sum();
        userCart.setTotalPrice(totalPrice);

        userCartRepository.save(userCart);
    }


    public void updateProductQuantityInUserCart(Principal connectedUser, Product product, int newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        UserCart userCart = userCartRepository.findUserCartByUser_Id(user.getId())
                .orElseThrow(() -> new IllegalStateException("User cart not found"));

        // Find the cart product in the user's cart
        Optional<CartProduct> cartProductOpt = userCart.getCartProducts().stream()
                .filter(p -> p.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (cartProductOpt.isPresent()) {
            CartProduct cartProduct = cartProductOpt.get();
            if (newQuantity == 0) {
                // If the new quantity is zero, remove the product from the cart
                userCart.getCartProducts().remove(cartProduct);
            } else {
                // Update the quantity of the product
                cartProduct.setQuantity(newQuantity);
            }

            // Recalculate total price
            double totalPrice = userCart.getCartProducts().stream()
                    .mapToDouble(cp -> cp.getProduct().getPrice() * cp.getQuantity())
                    .sum();
            userCart.setTotalPrice(totalPrice);

            userCartRepository.save(userCart);
        } else {
            throw new IllegalArgumentException("Product not found in cart");
        }
    }


    public void removeProductFromUserCart(Principal connectedUser, Product product, int quantity) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        UserCart userCart = userCartRepository.findUserCartByUser_Id(user.getId())
                .orElse(null);

        if (userCart == null) {
            throw new IllegalStateException("User cart not found");
        }

        // Find the cart product in the user's cart
        Optional<CartProduct> cartProductOpt = userCart.getCartProducts().stream()
                .filter(p -> p.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (cartProductOpt.isPresent()) {
            CartProduct cartProduct = cartProductOpt.get();
            if (cartProduct.getQuantity() > quantity) {
                // Reduce the quantity of the product
                cartProduct.setQuantity(cartProduct.getQuantity() - quantity);
            } else {
                // Remove the product from the cart if quantity to remove is equal or exceeds current quantity
                userCart.getCartProducts().remove(cartProduct);
            }

            // Recalculate total price
            double totalPrice = userCart.getCartProducts().stream()
                    .mapToDouble(cp -> cp.getProduct().getPrice() * cp.getQuantity())
                    .sum();
            userCart.setTotalPrice(totalPrice);

            userCartRepository.save(userCart);
        } else {
            throw new IllegalArgumentException("Product not found in cart");
        }
    }


    public void increaseProductQuantityInUserCart(Principal connectedUser, Product product, int increment) {
        if (increment <= 0) {
            throw new IllegalArgumentException("Increment must be positive");
        }

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        UserCart userCart = userCartRepository.findUserCartByUser_Id(user.getId())
                .orElseThrow(() -> new IllegalStateException("User cart not found"));

        Optional<CartProduct> cartProductOpt = userCart.getCartProducts().stream()
                .filter(p -> p.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (cartProductOpt.isPresent()) {
            CartProduct cartProduct = cartProductOpt.get();
            cartProduct.setQuantity(cartProduct.getQuantity() + increment);
        } else {
            throw new IllegalArgumentException("Product not found in cart");
        }

        updateTotalPrice(userCart);
    }


    public void decreaseProductQuantityInUserCart(Principal connectedUser, Product product, int decrement) {
        if (decrement <= 0) {
            throw new IllegalArgumentException("Decrement must be positive");
        }

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        UserCart userCart = userCartRepository.findUserCartByUser_Id(user.getId())
                .orElseThrow(() -> new IllegalStateException("User cart not found"));

        Optional<CartProduct> cartProductOpt = userCart.getCartProducts().stream()
                .filter(p -> p.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (cartProductOpt.isPresent()) {
            CartProduct cartProduct = cartProductOpt.get();
            int newQuantity = cartProduct.getQuantity() - decrement;
            if (newQuantity > 0) {
                cartProduct.setQuantity(newQuantity);
            } else {
                // Remove product if the new quantity is zero or less
                userCart.getCartProducts().remove(cartProduct);
            }
        } else {
            throw new IllegalArgumentException("Product not found in cart");
        }

        updateTotalPrice(userCart);
    }


    private void updateTotalPrice(UserCart userCart) {
        double totalPrice = userCart.getCartProducts().stream()
                .mapToDouble(cp -> cp.getProduct().getPrice() * cp.getQuantity())
                .sum();
        userCart.setTotalPrice(totalPrice);
        userCartRepository.save(userCart);
    }




    public CartResponse getByUserId(Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        UserCart userCart = userCartRepository.findUserCartByUser_Id(user.getId()).orElseThrow(() -> new RuntimeException("Cart not found for user."));
        CartResponse cartResponse = CartResponse.builder()
                .cartId(userCart.getId())
                .cartProducts(userCart.getCartProducts().stream()
                        .map(product -> new CartProductResponse(
                                product.getProduct().getId(),
                                product.getProduct().getBrand(),
                                product.getProduct().getTitle(),
                                product.getProduct().getCategory(),
                                product.getQuantity(),
                                product.getTotalPrice()))
                        .collect(Collectors.toList()))
                .totalPrice(userCart.getTotalPrice())
                .build();

        return cartResponse;
    }
}
