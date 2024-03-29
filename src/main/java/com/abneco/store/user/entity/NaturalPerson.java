package com.abneco.store.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NaturalPerson extends User {

    private String cpf;

    public NaturalPerson(String email, String cpf, String name, String password, Long phoneNumber, boolean emailVerified) {
        this.setEmail(email);
        this.cpf = cpf;
        this.setName(name);
        this.setPassword(password);
        this.setPhoneNumber(phoneNumber);
        this.setEmailVerified(emailVerified);
    }
}
