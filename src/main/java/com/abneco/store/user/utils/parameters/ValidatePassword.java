package com.abneco.store.user.utils.parameters;

import com.abneco.store.exception.RequestException;
import com.abneco.store.user.json.buyer.BuyerForm;
import com.abneco.store.user.json.seller.BaseSellerForm;
import com.abneco.store.user.json.seller.SellerForm;
import com.abneco.store.user.json.user.BaseUserForm;

public class ValidatePassword extends Validate {

    public ValidatePassword(Validate nextToValidate) {
        super(nextToValidate);
    }

    @Override
    public void validate(BuyerForm form) {
        if (form.getPassword() == null || form.getPassword().length() < 8) {
            throw new RequestException("Password must be at least 8 char long.");
        }
        nextToValidate.validate(form);
    }

    @Override
    public void validate(BaseUserForm form) {
        //BaseUserForm does not have a password
    }

    @Override
    public void validate(BaseSellerForm form) {
        //BaseSellerForm does not have a password
    }

    @Override
    public void validate(SellerForm form) {
        if (form.getPassword() == null || form.getPassword().length() < 8) {
            throw new RequestException("Password must be at least 8 char long.");
        }
        nextToValidate.validate(form);
    }
}
