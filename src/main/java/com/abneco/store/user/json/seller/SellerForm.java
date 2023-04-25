package com.abneco.store.user.json.seller;

import com.abneco.store.user.entity.Seller;
import com.abneco.store.utils.DateFormatter;
import com.abneco.store.utils.UpperCaseFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
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
