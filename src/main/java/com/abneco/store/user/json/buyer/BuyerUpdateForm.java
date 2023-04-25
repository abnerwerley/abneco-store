package com.abneco.store.user.json.buyer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyerUpdateForm extends BuyerForm {

    private String id;

    public BuyerUpdateForm(String id, String newName, String email, Long phoneNumber, String cpf) {
        setId(id);
        setName(newName);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setCpf(cpf);
    }
}
