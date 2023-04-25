package com.abneco.store.user.json.seller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
