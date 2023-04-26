package com.abneco.store.user.entity;

import com.abneco.store.address.entity.Address;
import com.abneco.store.product.entity.Product;
import com.abneco.store.user.json.seller.SellerResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
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
