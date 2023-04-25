package com.abneco.store.product.json;

import com.abneco.store.product.entity.Product;
import com.abneco.store.user.entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ProductForm {

    private String name;
    private String description;
    private BigDecimal price;
    private String sellerId;

    public Product toEntity(Seller seller) {
        Product product = new Product();

        product.setName(getName());
        product.setDescription(getDescription());
        product.setPrice(getPrice());
        product.setSeller(seller);
        return product;
    }
}
