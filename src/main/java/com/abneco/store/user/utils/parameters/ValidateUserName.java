package com.abneco.store.user.utils.parameters;

import com.abneco.store.exception.RequestException;
import com.abneco.store.user.json.buyer.BuyerForm;
import com.abneco.store.user.json.seller.BaseSellerForm;
import com.abneco.store.user.json.seller.SellerForm;
import com.abneco.store.user.json.user.BaseUserForm;

public class ValidateUserName extends Validate {

    public static final String ERROR_MESSAGE = "Name must be neither null nor shorter than 3.";

    public ValidateUserName(Validate nextToValidate) {
        super(nextToValidate);
    }

    @Override
    public void validate(BuyerForm form) {
        if (form.getName() == null || form.getName().length() < 3) {
            throw new RequestException(ERROR_MESSAGE);
        }
        nextToValidate.validate(form);
    }

    @Override
    public void validate(BaseUserForm form) {
        if (form.getName() == null || form.getName().length() < 3) {
            throw new RequestException(ERROR_MESSAGE);
        }
        nextToValidate.validate(form);
    }

    @Override
    public void validate(BaseSellerForm form) {
        if (form.getName() == null || form.getName().length() < 3) {
            throw new RequestException(ERROR_MESSAGE);
        }
        nextToValidate.validate(form);
    }

    @Override
    public void validate(SellerForm form) {
        if (form.getName() == null || form.getName().length() < 3) {
            throw new RequestException(ERROR_MESSAGE);
        }
        nextToValidate.validate(form);
    }
}
