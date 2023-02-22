package com.abneco.delivery.user.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "SELLER")
@Getter
@Setter
public class Seller extends JuridicalPerson {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "email")
    private String email;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "phoneNumber")
    private Long phoneNumber;

    @Column(name = "emailVerified")
    private Boolean emailVerified;

    @Column(name = "createdAt")
    private String createdAt;

    @Column(name = "updatedAt")
    private String updatedAt;
}
