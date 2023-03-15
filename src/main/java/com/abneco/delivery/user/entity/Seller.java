package com.abneco.delivery.user.entity;

import com.abneco.delivery.address.entity.Address;
import com.abneco.delivery.user.json.SellerResponse;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "SELLER")
@Getter
@Setter
public class Seller extends JuridicalPerson {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "seller_id")
    private String id;

    @OneToOne(mappedBy = "seller", cascade = CascadeType.ALL)
    private Address address;

    public Seller(String id, JuridicalPerson user, String createdAt, String updatedAt) {
        this.id = id;
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
        this.id = id;
        this.setEmail(user.getEmail());
        this.setCnpj(user.getCnpj());
        this.setName(user.getName());
        this.setPassword(user.getPassword());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setEmailVerified(user.getEmailVerified());
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
        this.address = address;
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
