package com.abneco.delivery.user.json.seller;

import com.abneco.delivery.user.json.user.BaseUserForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BaseSellerForm extends BaseUserForm {

    private String cnpj;
}
