package com.abneco.delivery.purchase.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductQuantity {

    private String productId;
    private int quantity;
}
