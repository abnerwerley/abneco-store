package com.abneco.delivery.user.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSellerForm extends BaseSellerForm {

    private String id;

    public UpdateSellerForm(String id, String newName, String email, long phoneNumber, String cnpj) {
        setId(id);
        setName(newName);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setCnpj(cnpj);
    }
}
