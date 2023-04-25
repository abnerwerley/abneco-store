package com.abneco.store.product.json;

import com.abneco.store.product.entity.Product;
import com.abneco.store.user.entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductForm {

    private String productId;
    private String name;
    private String description;
    private BigDecimal price;

    public Product toEntity(Seller seller) {
        Product product = new Product();

        product.setName(getName());
        product.setDescription(getDescription());
        product.setPrice(getPrice());
        product.setSeller(seller);
        return product;
    }
}
