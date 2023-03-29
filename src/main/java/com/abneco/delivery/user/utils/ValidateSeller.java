package com.abneco.delivery.user.utils;

import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.user.json.seller.SellerForm;
import com.abneco.delivery.user.json.seller.UpdateSellerForm;

public class ValidateSeller {

    public static void validateSeller(SellerForm form) {
        ValidateEmail.validateEmail(form.getEmail());
        if (form.getCnpj() == null || form.getCnpj().length() != 14) {
            throw new RequestException("Cnpj must have 14 numbers, and numbers only.");
        }
        if (form.getName() == null || form.getName().length() < 3) {
            throw new RequestException("Name must be neither null nor shorter than 3.");
        }
        if (form.getPassword().length() < 8) {
            throw new RequestException("Password must be at least 8 char long.");
        }
    }

    public static void validateSeller(UpdateSellerForm form) {
        ValidateEmail.validateEmail(form.getEmail());
        if (form.getCnpj() == null || form.getCnpj().length() != 14) {
            throw new RequestException("Cnpj must have 14 numbers, and numbers only.");
        }
        if (form.getName() == null || form.getName().length() < 3) {
            throw new RequestException("Name must be neither null nor shorter than 3.");
        }
    }

    private ValidateSeller() {
    }
}
