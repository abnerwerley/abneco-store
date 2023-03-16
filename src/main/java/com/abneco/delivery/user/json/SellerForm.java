package com.abneco.delivery.user.json;

import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.utils.DateFormatter;
import com.abneco.delivery.utils.UpperCaseFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellerForm extends BaseSellerForm {

    private String password;

    public SellerForm(String sellerName, String email, String password, Long phoneNumber, String cnpj) {
        setName(sellerName);
        setEmail(email);
        setPassword(password);
        setPhoneNumber(phoneNumber);
        setCnpj(cnpj);
    }

    public Seller toEntity() {
        Seller seller = new Seller();

        seller.setName(UpperCaseFormatter.formatToCapitalLetter(getName()));
        seller.setEmail(getEmail());
        seller.setPassword(getPassword());
        seller.setPhoneNumber(getPhoneNumber());
        seller.setCnpj(getCnpj());
        seller.setEmailVerified(false);
        seller.setUpdatedAt(DateFormatter.formatNow());
        seller.setCreatedAt(DateFormatter.formatNow());
        seller.setProducts(new ArrayList<>());

        return seller;
    }
}
