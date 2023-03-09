package com.abneco.delivery.address.entity;

import com.abneco.delivery.address.dto.AddressForm;
import com.abneco.delivery.address.dto.AddressResponse;
import com.abneco.delivery.user.entity.Seller;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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

    @OneToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;


    public Address(Seller user, AddressForm form, String logradouro, String bairro, String cidade, String uf) {
        this.seller = user;
        this.cep = form.getCep();
        this.logradouro = logradouro;
        this.complemento = form.getComplemento();
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.numero = form.getNumero();
    }

    public AddressResponse toResponse(String userId) {
        AddressResponse response = new AddressResponse();
        response.setAddressId(this.id);
        response.setUserId(userId);
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
