package com.abneco.delivery.user.entity;

import com.abneco.delivery.purchase.entity.Purchase;
import com.abneco.delivery.user.json.buyer.BuyerResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "BUYER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Buyer extends NaturalPerson {

    @OneToMany(mappedBy = "buyer")
    private List<Purchase> purchases = new ArrayList<>();

    public Buyer(NaturalPerson user, String createdAt, String updatedAt) {
        this.setEmail(user.getEmail());
        this.setCpf(user.getCpf());
        this.setName(user.getName());
        this.setPassword(user.getPassword());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setEmailVerified(user.getEmailVerified());
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
    }

    public Buyer(String id, NaturalPerson user, String createdAt, String updatedAt) {
        this.setId(id);
        this.setEmail(user.getEmail());
        this.setCpf(user.getCpf());
        this.setName(user.getName());
        this.setPassword(user.getPassword());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setEmailVerified(user.getEmailVerified());
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
    }

    public void setPurchases(Purchase purchase) {
        this.purchases.add(purchase);
    }

    public BuyerResponse toResponse() {
        return BuyerResponse.builder()
                .id(getId())
                .name(getName())
                .email(getEmail())
                .phoneNumber(getPhoneNumber())
                .cpf(getCpf())
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }
}
