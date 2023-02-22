package com.abneco.delivery.utils;

import com.abneco.delivery.exception.RequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidateEmailTest {

    public static final String NORMAL = "name@email.com";
    public static final String ORG = "name@email.org";
    public static final String HYPHEN_AMID = "user-name@domain.com";
    public static final String UNDERLINE_AMID = "user_name@domain.com";
    public static final String WITHOUT_AT = "nameemail.org";
    public static final String JUST_LETTERS = "nameemailcom";
    public static final String WITHOUT_DOT = "name@email";
    public static final String ENDING_WITH_DOT = "name@email.";
    public static final String WITHOUT_DOMAIN = "username@.com";

    @Test
    void testValidateEmail() {
        assertDoesNotThrow(() -> ValidateEmail.validateEmail(NORMAL));
        assertDoesNotThrow(() -> ValidateEmail.validateEmail(ORG));
        assertDoesNotThrow(() -> ValidateEmail.validateEmail(HYPHEN_AMID));
        assertDoesNotThrow(() -> ValidateEmail.validateEmail(UNDERLINE_AMID));
    }

    @Test
    void testValidateEmailExceptions(){
        Exception withoutAt = assertThrows(RequestException.class, () -> ValidateEmail.validateEmail(WITHOUT_AT));
        assertEquals("Email has incorrect format.", withoutAt.getMessage());

        Exception justLetters = assertThrows(RequestException.class, () -> ValidateEmail.validateEmail(JUST_LETTERS));
        assertEquals("Email has incorrect format.", justLetters.getMessage());

        Exception withoutDot = assertThrows(RequestException.class, () -> ValidateEmail.validateEmail(WITHOUT_DOT));
        assertEquals("Email has incorrect format.", withoutDot.getMessage());

        Exception endingWithDot = assertThrows(RequestException.class, () -> ValidateEmail.validateEmail(ENDING_WITH_DOT));
        assertEquals("Email has incorrect format.", endingWithDot.getMessage());

        Exception withoutDomain = assertThrows(RequestException.class, () -> ValidateEmail.validateEmail(WITHOUT_DOMAIN));
        assertEquals("Email has incorrect format.", withoutDomain.getMessage());
    }
}
