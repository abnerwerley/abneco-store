package com.abneco.store.product.json;

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
}
