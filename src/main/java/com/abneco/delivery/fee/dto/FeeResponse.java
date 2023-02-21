package com.abneco.delivery.fee.dto;

import com.abneco.delivery.address.dto.AddressTO;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeeResponse {
    private String cep;
    private String rua;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private BigDecimal frete;

    public FeeResponse(AddressTO addressTO, BigDecimal frete) {
        this.cep = addressTO.getCep();
        this.rua = addressTO.getLogradouro();
        this.complemento = addressTO.getComplemento();
        this.bairro = addressTO.getBairro();
        this.cidade = addressTO.getLocalidade();
        this.estado = addressTO.getUf();
        this.frete = frete;
    }
}
