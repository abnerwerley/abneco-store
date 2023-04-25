package com.abneco.store.address.json;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AddressUpdateForm extends BaseAddressForm {

    @NotNull
    private String addressId;

    public AddressUpdateForm(String addressId, String sellerId, String newCep, String novoComplemento, Integer novoNumero) {
        this.addressId = addressId;
        setUserId(sellerId);
        setCep(newCep);
        setComplemento(novoComplemento);
        setNumero(novoNumero);
    }
}
