package com.abneco.delivery.address.json;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@Builder
public class AddressForm {

    @NotNull
    private String userId;
    @NotNull
    @Size(min = 8, max = 8)
    private String cep;
    private String complemento;

    @NotNull
    private Integer numero;
}
