package com.abneco.delivery.user.json;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
