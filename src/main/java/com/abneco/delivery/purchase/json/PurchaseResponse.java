package com.abneco.delivery.purchase.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PurchaseResponse {

    private String purchaseId;
    private String buyerId;
    private List<ProductQuantity> productQuantityList;
    private BigDecimal finalPrice;
}
