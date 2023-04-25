package com.abneco.store.purchase.entity;

import com.abneco.store.product.repository.ProductRepository;
import com.abneco.store.purchase.json.ProductQuantity;
import com.abneco.store.purchase.json.PurchasePerProduct;
import com.abneco.store.purchase.json.PurchaseResponse;
import com.abneco.store.purchase.utils.ProductPurchaseConverter;
import com.abneco.store.user.entity.Buyer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Purchase {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "purchase_id")
    private String id;

    @ManyToOne
    private Buyer buyer;

    @ManyToMany
    @NotNull
    @JoinTable(
            name = "purchase_purchase_per_product",
            joinColumns = @JoinColumn(name = "purchase_id"),
            inverseJoinColumns = @JoinColumn(name = "purchase_per_product_id"))
    private List<PurchasePerProduct> purchasesPerProducts = new ArrayList<>();

    @NotNull
    private BigDecimal finalPrice;

    @Column(name = "purchasedAt")
    private String purchasedAt;

    public PurchaseResponse toResponse(ProductRepository productRepository) {
        PurchaseResponse response = new PurchaseResponse();
        ProductPurchaseConverter converter = new ProductPurchaseConverter(productRepository);

        List<ProductQuantity> productQuantityList = converter.convertToProductQuantity(purchasesPerProducts);
        response.setPurchaseId(this.id);
        response.setBuyerId(this.buyer.getId());
        response.setProductQuantityList(productQuantityList);
        response.setFinalPrice(this.finalPrice);
        return response;
    }
}
