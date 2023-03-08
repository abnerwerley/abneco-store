package com.abneco.delivery.address.entity;

import com.abneco.delivery.address.dto.AddressResponse;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "ADDRESS")
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "address_id")
    private String id;

    @NotNull
    @Size(min = 8, max = 8)
    private String cep;

    @NotNull
    private String logradouro;

    private String complemento;

    @NotNull
    private String bairro;

    @NotNull
    private String cidade;

    @NotNull
    @Size(max = 2)
    private String uf;

    @NotNull
    private Integer numero;

    public Address(String cep, String logradouro, String complemento, String bairro, String cidade, String uf, Integer numero) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.numero = numero;
    }

    public AddressResponse toResponse() {
        AddressResponse response = new AddressResponse();
        response.setCep(this.cep);
        response.setLogradouro(this.logradouro);
        response.setComplemento(this.complemento);
        response.setBairro(this.bairro);
        response.setCidade(this.cidade);
        response.setNumero(this.numero);
        response.setUf(this.uf);
        return response;
    }

}
