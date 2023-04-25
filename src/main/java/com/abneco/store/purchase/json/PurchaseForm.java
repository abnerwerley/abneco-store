package com.abneco.store.purchase.json;

import com.abneco.store.purchase.entity.Purchase;
import com.abneco.store.user.entity.Buyer;
import com.abneco.store.utils.DateFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseForm {

    private String buyerId;
    private List<ProductQuantity> productAndQuantityList;

    public Purchase toEntity(Buyer buyer, List<PurchasePerProduct> purchasePerProducts, BigDecimal finalPrice) {
        Purchase purchase = new Purchase();
        purchase.setBuyer(buyer);
        purchase.setPurchasesPerProducts(purchasePerProducts);
        purchase.setFinalPrice(finalPrice);
        purchase.setPurchasedAt(DateFormatter.formatNow());

        return purchase;
    }
}
