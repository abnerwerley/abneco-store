package com.abneco.delivery.address.dto;

import lombok.*;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

    private String addressId;
    private String userId;
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private Integer numero;
}
