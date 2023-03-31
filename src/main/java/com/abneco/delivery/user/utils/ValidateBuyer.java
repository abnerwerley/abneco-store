package com.abneco.delivery.user.utils;

import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.user.json.buyer.BuyerForm;
import com.abneco.delivery.user.json.buyer.BuyerUpdateForm;

public class ValidateBuyer {

    public static void validateBuyer(BuyerForm form) {
        ValidateEmail.validateEmail(form.getEmail());
        if (form.getCpf() == null || form.getCpf().length() != 11) {
            throw new RequestException("Cpf must have 11 numbers, and numbers only.");
        }
        if (form.getName() == null || form.getName().length() < 3) {
            throw new RequestException("Name must be neither null nor shorter than 3.");
        }
        if (form.getPassword().length() < 8) {
            throw new RequestException("Password must be at least 8 char long.");
        }
    }
    public static void validateBuyer(BuyerUpdateForm form) {
        ValidateEmail.validateEmail(form.getEmail());
        if (form.getCpf() == null || form.getCpf().length() != 11) {
            throw new RequestException("Cpf must have 11 numbers, and numbers only.");
        }
        if (form.getName() == null || form.getName().length() < 3) {
            throw new RequestException("Name must be neither null nor shorter than 3.");
        }
    }

    private ValidateBuyer() {
    }
}
