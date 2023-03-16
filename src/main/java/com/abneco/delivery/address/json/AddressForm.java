package com.abneco.delivery.address.json;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddressForm extends BaseAddressForm {
    public AddressForm(String id, String cep, String complemento, Integer numero) {
        super(id, cep, complemento, numero);
    }
}
