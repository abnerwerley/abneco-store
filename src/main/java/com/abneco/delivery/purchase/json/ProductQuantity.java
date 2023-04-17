package com.abneco.delivery.purchase.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProductQuantity {

    private String productId;
    private int quantity;
}
