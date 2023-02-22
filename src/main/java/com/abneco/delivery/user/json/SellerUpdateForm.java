package com.abneco.delivery.user.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellerUpdateForm {

    private String id;
    private String name;
    private String email;
    private Long phoneNumber;
    private String cnpj;
}
