package com.abneco.store.user.json.seller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SellerResponse {

    private String id;
    private String name;
    private String email;
    private Long phoneNumber;
    private String cnpj;
    private String createdAt;
    private String updatedAt;
}
