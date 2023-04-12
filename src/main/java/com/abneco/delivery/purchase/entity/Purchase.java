package com.abneco.delivery.purchase.entity;

import com.abneco.delivery.product.entity.Product;
import com.abneco.delivery.user.entity.Buyer;
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
            name = "purchase_product",
            joinColumns = @JoinColumn(name = "purchase_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products = new ArrayList<>();

    @NotNull
    private int quantity;

    @NotNull
    private BigDecimal finalPrice;

    @Column(name = "purchasedAt")
    private String purchasedAt;
}
