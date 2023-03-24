package com.abneco.delivery.product.entity;

import com.abneco.delivery.product.json.ProductResponse;
import com.abneco.delivery.user.entity.Seller;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "PRODUCT")
@Getter
@Setter
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "product_id")
    private String id;

    @NotNull
    private String name;

    @Size(min = 10)
    private String description;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "user_id")
    private Seller seller;

    public Product(String name, String description, BigDecimal price, Seller seller) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.seller = seller;
    }

    public ProductResponse toResponse() {
        ProductResponse response = new ProductResponse();
        response.setProductId(getId());
        response.setName(getName());
        response.setDescription(getDescription());
        response.setPrice(getPrice());
        response.setSellerId(getSeller().getId());
        return response;
    }
}
