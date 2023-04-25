package com.abneco.store.product.json;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductResponse {

    private String productId;
    private String sellerId;
    private String name;
    private String description;
    private BigDecimal price;
}
