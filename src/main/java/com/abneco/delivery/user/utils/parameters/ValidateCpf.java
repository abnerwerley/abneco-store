package com.abneco.delivery.user.utils.parameters;

import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.user.json.buyer.BuyerForm;
import com.abneco.delivery.user.json.seller.BaseSellerForm;
import com.abneco.delivery.user.json.seller.SellerForm;
import com.abneco.delivery.user.json.user.BaseUserForm;

public class ValidateCpf extends Validate {

    public ValidateCpf(Validate nextToValidate) {
        super(nextToValidate);
    }

    @Override
    public void validate(BuyerForm form) {
        if (form.getCpf() == null || form.getCpf().length() != 11) {
            throw new RequestException("Cpf must have 11 numbers, and numbers only.");
        }
        nextToValidate.validate(form);
    }

    public void validate(BaseUserForm form) {
        //BaseUserForm does not have cpf field
    }

    @Override
    public void validate(BaseSellerForm form) {
        //BaseSellerForm does not have cpf field
    }

    @Override
    public void validate(SellerForm form) {
        //SellerForm does not have cpf field
    }
}
