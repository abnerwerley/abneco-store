package com.abneco.store.user.json.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseUserForm {

    private String name;
    private String email;
    private Long phoneNumber;
}
