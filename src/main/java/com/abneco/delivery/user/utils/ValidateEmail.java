package com.abneco.delivery.user.utils;

import com.abneco.delivery.exception.RequestException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateEmail {
    public static void validateEmail(String email) {
        String emailRegex = ".+@.+[.].+";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new RequestException("Email has incorrect format.");
        }
    }

    private ValidateEmail() {
    }
}
