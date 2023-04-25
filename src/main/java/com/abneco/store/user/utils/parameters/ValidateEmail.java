package com.abneco.store.user.utils.parameters;

import com.abneco.store.exception.RequestException;
import com.abneco.store.user.json.buyer.BuyerForm;
import com.abneco.store.user.json.seller.BaseSellerForm;
import com.abneco.store.user.json.seller.SellerForm;
import com.abneco.store.user.json.user.BaseUserForm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateEmail extends Validate {

    public static final String EMAIL_INCORRECT_MESSAGE = "Email has incorrect format.";

    public ValidateEmail(Validate nextToValidate) {
        super(nextToValidate);
    }

    private boolean isEmailValid(String email) {
        String emailRegex = ".+@.+[.].+";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }

    @Override
    public void validate(BuyerForm form) {
        if (isEmailValid(form.getEmail())) {
            throw new RequestException(EMAIL_INCORRECT_MESSAGE);
        }
        nextToValidate.validate(form);
    }

    @Override
    public void validate(BaseUserForm form) {
        if (isEmailValid(form.getEmail())) {
            throw new RequestException(EMAIL_INCORRECT_MESSAGE);
        }
        nextToValidate.validate(form);
    }

    @Override
    public void validate(BaseSellerForm form) {
        if (isEmailValid(form.getEmail())) {
            throw new RequestException(EMAIL_INCORRECT_MESSAGE);
        }
        nextToValidate.validate(form);
    }

    @Override
    public void validate(SellerForm form) {
        if (isEmailValid(form.getEmail())) {
            throw new RequestException(EMAIL_INCORRECT_MESSAGE);
        }
        nextToValidate.validate(form);
    }
}
