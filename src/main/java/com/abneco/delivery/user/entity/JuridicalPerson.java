package com.abneco.delivery.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class JuridicalPerson extends User {

    @NotNull
    @Column(name = "cnpj", unique = true)
    private String cnpj;

    public JuridicalPerson(String email, String cnpj, String name, String password, Long phoneNumber, boolean emailVerified) {
        this.setEmail(email);
        this.cnpj = cnpj;
        this.setName(name);
        this.setPassword(password);
        this.setPhoneNumber(phoneNumber);
        this.setEmailVerified(emailVerified);
    }
}
