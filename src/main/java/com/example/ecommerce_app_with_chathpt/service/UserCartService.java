package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.Product;
import com.example.ecommerce_app_with_chathpt.model.User;
import com.example.ecommerce_app_with_chathpt.model.UserCart;
import com.example.ecommerce_app_with_chathpt.repository.UserCartRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class UserCartService {

    private UserCartRepository userCartRepository;


    public void addProductToUserCart(Principal connectedUser, Product product) {
        //TODO add quantity mechanism
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        UserCart userCart = userCartRepository.findUserCartByUser_Id(user.getId())
                .orElseGet(() -> {
                    UserCart newUserCart = new UserCart();
                    newUserCart.setCartProducts(new ArrayList<Product>());
                    newUserCart.setTotalPrice(0.0);
                    newUserCart.setUser(user);
                    return newUserCart;
                });
        userCart.getCartProducts().add(product);
        userCart.setTotalPrice(userCart.getTotalPrice()+product.getPrice());
        userCartRepository.save(userCart);
    }

    public Optional<UserCart> getByUserId(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return userCartRepository.findUserCartByUser_Id(user.getId());
    }
}
