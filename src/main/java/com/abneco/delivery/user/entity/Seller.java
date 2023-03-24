package com.abneco.delivery.user.entity;

import com.abneco.delivery.address.entity.Address;
import com.abneco.delivery.product.entity.Product;
import com.abneco.delivery.user.json.SellerResponse;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "SELLER")
@Getter
@Setter
public class Seller extends JuridicalPerson {

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    public Seller(String id, JuridicalPerson user, String createdAt, String updatedAt) {
        this.setId(id);
        this.setEmail(user.getEmail());
        this.setCnpj(user.getCnpj());
        this.setName(user.getName());
        this.setPassword(user.getPassword());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setEmailVerified(user.getEmailVerified());
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
    }

    public Seller(String id, JuridicalPerson user, String createdAt, String updatedAt, Address address) {
        this.setId(id);
        this.setEmail(user.getEmail());
        this.setCnpj(user.getCnpj());
        this.setName(user.getName());
        this.setPassword(user.getPassword());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setEmailVerified(user.getEmailVerified());
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
        this.setAddress(address);
    }

    public Seller(JuridicalPerson user, String createdAt, String updatedAt) {
        this.setEmail(user.getEmail());
        this.setCnpj(user.getCnpj());
        this.setName(user.getName());
        this.setPassword(user.getPassword());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setEmailVerified(user.getEmailVerified());
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
    }

    public SellerResponse toResponse() {
        return SellerResponse.builder()
                .id(getId())
                .name(getName())
                .email(getEmail())
                .phoneNumber(getPhoneNumber())
                .cnpj(getCnpj())
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }
}
