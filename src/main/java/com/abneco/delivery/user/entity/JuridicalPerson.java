package com.abneco.delivery.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@MappedSuperclass
public class JuridicalPerson extends User {

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
