package com.abneco.delivery.address.entity;

import com.abneco.delivery.address.json.AddressForm;
import com.abneco.delivery.address.json.AddressResponse;
import com.abneco.delivery.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
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
    @JoinColumn(name = "user_fk")
    private User user;


    public Address(User user, AddressForm form, String logradouro, String bairro, String cidade, String uf) {
        this.user = user;
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
        response.setAddressId(getId());
        response.setUserId(userId);
        response.setCep(getCep());
        response.setLogradouro(getLogradouro());
        response.setComplemento(getComplemento());
        response.setBairro(getBairro());
        response.setCidade(getCidade());
        response.setNumero(getNumero());
        response.setUf(getUf());
        return response;
    }

}
