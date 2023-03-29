package com.abneco.delivery.user.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class NaturalPerson extends User {

    private String cpf;
}
