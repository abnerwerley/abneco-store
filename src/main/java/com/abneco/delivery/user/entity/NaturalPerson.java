package com.abneco.delivery.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaturalPerson extends User {

    private Long cpf;
}
