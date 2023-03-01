package com.abneco.delivery.user.json;

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
}
