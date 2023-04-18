package com.abneco.delivery.utils;

import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.user.json.user.BaseUserForm;
import com.abneco.delivery.user.utils.parameters.NothingToValidate;
import com.abneco.delivery.user.utils.parameters.ValidateEmail;
import com.abneco.delivery.user.utils.parameters.ValidateUserName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidateEmailTest {

    @InjectMocks
    private ValidateEmail validateEmail;

    public static final String NORMAL = "name@email.com";
    public static final String ORG = "name@email.org";
    public static final String HYPHEN_AMID = "user-name@domain.com";
    public static final String UNDERLINE_AMID = "user_name@domain.com";
    public static final String WITHOUT_AT = "nameemail.org";
    public static final String JUST_LETTERS = "nameemailcom";
    public static final String WITHOUT_DOT = "name@email";
    public static final String ENDING_WITH_DOT = "name@email.";
    public static final String WITHOUT_DOMAIN = "username@.com";
    public static final String NAME = "name";
    public static final Long PHONE_NUMBER = 12348765123L;

    @Test
    void testValidateEmail() {
        this.validateEmail = new ValidateEmail(new ValidateUserName(new NothingToValidate()));

        BaseUserForm normal = new BaseUserForm(NAME, NORMAL, PHONE_NUMBER);
        assertDoesNotThrow(() -> validateEmail.validate(normal));

        BaseUserForm org = new BaseUserForm(NAME, ORG, PHONE_NUMBER);
        assertDoesNotThrow(() -> validateEmail.validate(org));

        BaseUserForm hyphen = new BaseUserForm(NAME, HYPHEN_AMID, PHONE_NUMBER);
        assertDoesNotThrow(() -> validateEmail.validate(hyphen));

        BaseUserForm underline = new BaseUserForm(NAME, UNDERLINE_AMID, PHONE_NUMBER);
        assertDoesNotThrow(() -> validateEmail.validate(underline));
    }

    @Test
    void testValidateEmailExceptions() {
        this.validateEmail = new ValidateEmail(new ValidateUserName(new NothingToValidate()));

        BaseUserForm withoutAtForm = new BaseUserForm(NAME, WITHOUT_AT, PHONE_NUMBER);
        Exception withoutAt = assertThrows(RequestException.class, () -> validateEmail.validate(withoutAtForm));
        assertEquals("Email has incorrect format.", withoutAt.getMessage());

        BaseUserForm justLettersForm = new BaseUserForm(NAME, JUST_LETTERS, PHONE_NUMBER);
        Exception justLetters = assertThrows(RequestException.class, () -> validateEmail.validate(justLettersForm));
        assertEquals("Email has incorrect format.", justLetters.getMessage());

        BaseUserForm withoutDotForm = new BaseUserForm(NAME, WITHOUT_DOT, PHONE_NUMBER);
        Exception withoutDot = assertThrows(RequestException.class, () -> validateEmail.validate(withoutDotForm));
        assertEquals("Email has incorrect format.", withoutDot.getMessage());

        BaseUserForm endingWithDotForm = new BaseUserForm(NAME, ENDING_WITH_DOT, PHONE_NUMBER);
        Exception endingWithDot = assertThrows(RequestException.class, () -> validateEmail.validate(endingWithDotForm));
        assertEquals("Email has incorrect format.", endingWithDot.getMessage());

        BaseUserForm withoutDomainForm = new BaseUserForm(NAME, WITHOUT_DOMAIN, PHONE_NUMBER);
        Exception withoutDomain = assertThrows(RequestException.class, () -> validateEmail.validate(withoutDomainForm));
        assertEquals("Email has incorrect format.", withoutDomain.getMessage());
    }
}
