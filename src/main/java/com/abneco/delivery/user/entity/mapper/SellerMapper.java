package com.abneco.delivery.user.entity.mapper;

import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.json.SellerForm;
import com.abneco.delivery.utils.DateFormatter;

public class SellerMapper {

    public static Seller fromFormToSellerEntity(SellerForm form) {
        Seller seller = new Seller();

        seller.setName(form.getName());
        seller.setEmail(form.getEmail());
        seller.setPassword(form.getPassword());
        seller.setPhoneNumber(form.getPhoneNumber());
        seller.setCnpj(form.getCnpj());
        seller.setEmailVerified(false);
        seller.setUpdatedAt(DateFormatter.formatNow());
        seller.setCreatedAt(DateFormatter.formatNow());

        return seller;
    }

    private SellerMapper() {
    }
}
