package com.abneco.delivery.product.json;

import com.abneco.delivery.product.entity.Product;
import com.abneco.delivery.user.entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
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
