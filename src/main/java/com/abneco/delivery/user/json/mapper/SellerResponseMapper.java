package com.abneco.delivery.user.json.mapper;

import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.json.SellerResponse;
import lombok.Getter;

@Getter
public class SellerResponseMapper {

    public static SellerResponse fromEntityToResponse(Seller seller) {
        return SellerResponse.builder()
                .id(seller.getId())
                .name(seller.getName())
                .email(seller.getEmail())
                .phoneNumber(seller.getPhoneNumber())
                .cnpj(seller.getCnpj())
                .createdAt(seller.getCreatedAt())
                .updatedAt(seller.getUpdatedAt()).build();
    }

    private SellerResponseMapper() {
    }
}
