package com.abneco.delivery.user.utils.parameters;

import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.user.json.buyer.BuyerForm;
import com.abneco.delivery.user.json.seller.BaseSellerForm;
import com.abneco.delivery.user.json.seller.SellerForm;
import com.abneco.delivery.user.json.user.BaseUserForm;

public class ValidateCnpj extends Validate {

    public ValidateCnpj(Validate nextToValidate) {
        super(nextToValidate);
    }

    @Override
    public void validate(BuyerForm form) {
        //BuyerForm does not have a cnpj
    }


    @Override
    public void validate(BaseUserForm form) {
        //BaseUserForm does not have a cnpj
    }

    @Override
    public void validate(BaseSellerForm form) {
        if (form.getCnpj() == null || form.getCnpj().length() != 14) {
            throw new RequestException("Cnpj must have 14 numbers, and numbers only.");
        }
        nextToValidate.validate(form);
    }

    @Override
    public void validate(SellerForm form) {
        if (form.getCnpj() == null || form.getCnpj().length() != 14) {
            throw new RequestException("Cnpj must have 14 numbers, and numbers only.");
        }
        nextToValidate.validate(form);
    }
}
