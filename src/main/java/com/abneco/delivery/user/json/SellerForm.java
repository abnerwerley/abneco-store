package com.abneco.delivery.user.json;

import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.utils.DateFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellerForm {

    private String name;
    private String email;
    private String password;
    private Long phoneNumber;
    private String cnpj;

    public Seller toEntity() {
        Seller seller = new Seller();

        seller.setName(getName());
        seller.setEmail(getEmail());
        seller.setPassword(getPassword());
        seller.setPhoneNumber(getPhoneNumber());
        seller.setCnpj(getCnpj());
        seller.setEmailVerified(false);
        seller.setUpdatedAt(DateFormatter.formatNow());
        seller.setCreatedAt(DateFormatter.formatNow());

        return seller;
    }
}
