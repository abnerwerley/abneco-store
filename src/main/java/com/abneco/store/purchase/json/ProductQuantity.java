package com.abneco.store.purchase.json;

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
