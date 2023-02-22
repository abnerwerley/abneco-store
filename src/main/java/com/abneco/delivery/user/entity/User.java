package com.abneco.delivery.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class User {

    private String name;
    private String email;
    private String password;
    private Long phoneNumber;
    private Boolean emailVerified;
    private String createdAt;
    private String updatedAt;
}
