package com.abneco.store.user.json.buyer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BuyerResponse {
    private String id;
    private String name;
    private String email;
    private Long phoneNumber;
    private String cpf;
    private String createdAt;
    private String updatedAt;
}
