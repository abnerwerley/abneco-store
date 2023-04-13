package com.abneco.delivery.purchase.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "PURCHASE_PER_PRODUCT")
@Setter
public class PurchasePerProduct {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "purchase_per_product_id")
    private String id;
    private String productId;
    private int quantity;

    public PurchasePerProduct(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
