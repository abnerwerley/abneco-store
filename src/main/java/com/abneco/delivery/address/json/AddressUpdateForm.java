package com.abneco.delivery.address.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
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
