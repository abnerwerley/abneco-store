package com.abneco.delivery.purchase.json;

import com.abneco.delivery.product.entity.Product;
import com.abneco.delivery.purchase.entity.Purchase;
import com.abneco.delivery.user.entity.Buyer;
import com.abneco.delivery.utils.DateFormatter;
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
    private List<String> products;
    private int quantity;

    public Purchase toEntity(Buyer buyer, List<Product> products, BigDecimal finalPrice) {
        Purchase purchase = new Purchase();
        purchase.setBuyer(buyer);
        purchase.setProducts(products);
        purchase.setQuantity(this.quantity);
        purchase.setFinalPrice(finalPrice);
        purchase.setPurchasedAt(DateFormatter.formatNow());

        return purchase;
    }
}
