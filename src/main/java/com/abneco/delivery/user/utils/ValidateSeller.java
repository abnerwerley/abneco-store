package com.abneco.delivery.user.utils;

import com.abneco.delivery.user.json.seller.SellerForm;
import com.abneco.delivery.user.json.seller.UpdateSellerForm;
import com.abneco.delivery.user.utils.parameters.Validate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
@AllArgsConstructor
public class ValidateSeller {

    @Autowired
    private Validate validator;

    public void validateSeller(SellerForm form, Validate validator) {
        validator.validate(form);
    }

    public void validateSeller(UpdateSellerForm form, Validate validator) {
        validator.validate(form);
    }
}
