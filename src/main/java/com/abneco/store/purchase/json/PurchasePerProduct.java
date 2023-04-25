package com.abneco.store.purchase.json;

import com.abneco.store.product.entity.Product;
import com.abneco.store.purchase.entity.Purchase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity(name = "PURCHASE_PER_PRODUCT")
@Table(name = "PURCHASE_PER_PRODUCT")
public class PurchasePerProduct {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "purchase_per_product_id")
    private String id;

    @ManyToMany(mappedBy = "purchasesPerProducts")
    private final List<Purchase> purchases = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;


    public PurchasePerProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public PurchasePerProduct(String purchaseProductId, Product product, int quantity) {
        this.id = purchaseProductId;
        this.product = product;
        this.quantity = quantity;
    }
}
