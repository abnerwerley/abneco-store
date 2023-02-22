package com.abneco.delivery.user.entity.mapper;

import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.json.SellerForm;
import com.abneco.delivery.utils.DateFormatter;

public class SellerMapper {

    public static Seller fromFormToSellerEntity(SellerForm form) {
        return Seller.builder().name(form.getName())
                .email(form.getEmail())
                .cnpj(form.getCnpj())
                .password(form.getPassword())
                .phoneNumber(form.getPhoneNumber())
                .emailVerified(false)
                .createdAt(DateFormatter.formatNow())
                .updatedAt(DateFormatter.formatNow())
                .build();
    }

    private SellerMapper() {
    }
}
