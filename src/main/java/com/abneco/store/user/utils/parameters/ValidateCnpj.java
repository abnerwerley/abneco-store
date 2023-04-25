package com.abneco.store.user.utils.parameters;

import com.abneco.store.exception.RequestException;
import com.abneco.store.user.json.buyer.BuyerForm;
import com.abneco.store.user.json.seller.BaseSellerForm;
import com.abneco.store.user.json.seller.SellerForm;
import com.abneco.store.user.json.user.BaseUserForm;

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
