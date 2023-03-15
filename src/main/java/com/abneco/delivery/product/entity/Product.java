package com.abneco.delivery.product.entity;

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
    @JoinColumn(name = "seller_fk")
    private Seller seller;

}
