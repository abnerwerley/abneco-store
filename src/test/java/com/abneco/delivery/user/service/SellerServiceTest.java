package com.abneco.delivery.user.service;

import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.json.SellerForm;
import com.abneco.delivery.user.json.SellerResponse;
import com.abneco.delivery.user.json.SellerUpdateForm;
import com.abneco.delivery.user.repository.SellerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

    @InjectMocks
    private SellerService service;

    @Mock
    SellerRepository repository;

    public static final String ID = "iyu230hskdf-dfoi7-462c-a47f-7afaade01517";
    public static final String NAME = "Name";
    public static final String NEW_NAME = "Name";
    public static final String EMAIL = "name@email.com";
    public static final String NEW_EMAIL = "name@newemail.com";
    public static final String PASSWORD = "12345678";
    public static final String SHORT_PASSWORD = "1234567";
    public static final Long PHONE_NUMBER = 1112345678L;
    public static final String CNPJ = "09876543211234";
    public static final String SHORT_CNPJ = "0987654321123";
    public static final String LONG_CNPJ = "098765432112345";
    public static final String NEW_CNPJ = "49529348908734";
    public static final String EMAIL_WITHOUT_AT = "nameemail.org";
    public static final String SHORT_NAME = "Et";

    @Test
    void testRegisterSeller() {
        SellerForm form = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        when(repository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        service.registerSeller(form);
        verify(repository).findByEmail(EMAIL);
        verify(repository).save(any(Seller.class));
    }

    @Test
    void testRegisterSellerRequestException() {
        SellerForm form = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        doReturn(optionalSeller()).when(repository).findByEmail(EMAIL);
        Exception exception = assertThrows(RequestException.class, () -> service.registerSeller(form));
        assertNotNull(exception);
        assertEquals("Email already in use.", exception.getMessage());
        verify(repository, never()).save(Mockito.any(Seller.class));
    }

    @Test
    void testRegisterSellerWithNotAValidEmail() {
        SellerForm form = new SellerForm(NAME, EMAIL_WITHOUT_AT, PASSWORD, PHONE_NUMBER, CNPJ);
        Exception exception = assertThrows(RequestException.class, () -> service.registerSeller(form));
        assertNotNull(exception);
        assertEquals("Email has incorrect format.", exception.getMessage());
        verify(repository, never()).save(Mockito.any(Seller.class));
    }

    @Test
    void testRegisterSellerSaveValidations() {
        SellerForm shortCnpjForm = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, SHORT_CNPJ);
        when(repository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        Exception shortCnpj = assertThrows(RequestException.class, () -> service.registerSeller(shortCnpjForm));
        assertNotNull(shortCnpj);
        assertEquals("Cnpj must have 14 numbers, and numbers only.", shortCnpj.getMessage());

        SellerForm longCnpjForm = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, LONG_CNPJ);
        Exception longCnpj = assertThrows(RequestException.class, () -> service.registerSeller(longCnpjForm));
        assertNotNull(longCnpj);
        assertEquals("Cnpj must have 14 numbers, and numbers only.", longCnpj.getMessage());
        verify(repository, never()).save(Mockito.any(Seller.class));

        SellerForm nullCnpjForm = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, null);
        Exception nullCnpj = assertThrows(RequestException.class, () -> service.registerSeller(nullCnpjForm));
        assertNotNull(nullCnpj);
        assertEquals("Cnpj must have 14 numbers, and numbers only.", nullCnpj.getMessage());
        verify(repository, never()).save(Mockito.any(Seller.class));

        SellerForm shortNameForm = new SellerForm(SHORT_NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        Exception shortName = assertThrows(RequestException.class, () -> service.registerSeller(shortNameForm));
        assertNotNull(shortName);
        assertEquals("Name must be neither null nor shorter than 3.", shortName.getMessage());
        verify(repository, never()).save(Mockito.any(Seller.class));

        SellerForm nullNameForm = new SellerForm(SHORT_NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        Exception nullName = assertThrows(RequestException.class, () -> service.registerSeller(nullNameForm));
        assertNotNull(nullName);
        assertEquals("Name must be neither null nor shorter than 3.", nullName.getMessage());
        verify(repository, never()).save(Mockito.any(Seller.class));

        SellerForm shortPasswordForm = new SellerForm(NAME, EMAIL, SHORT_PASSWORD, PHONE_NUMBER, CNPJ);
        Exception shortPassword = assertThrows(RequestException.class, () -> service.registerSeller(shortPasswordForm));
        assertNotNull(shortPassword);
        assertEquals("Password must be at least 8 char long.", shortPassword.getMessage());
        verify(repository, never()).save(Mockito.any(Seller.class));
    }

    @Test
    void testUpdateSeller() {
        SellerUpdateForm form = new SellerUpdateForm(ID, NEW_NAME, NEW_EMAIL, PHONE_NUMBER, NEW_CNPJ);
        doReturn(optionalSeller()).when(repository).findById(ID);
        SellerResponse response = service.updateSeller(form);
        assertNotNull(response);
        assertEquals(NEW_NAME, response.getName());
        assertEquals(NEW_EMAIL, response.getEmail());
        assertEquals(NEW_CNPJ, response.getCnpj());
        verify(repository).findById(ID);
        verify(repository).save(any(Seller.class));
    }

    @Test
    void testUpdateSellerSellerNotFound() {
        SellerUpdateForm form = new SellerUpdateForm(ID, NEW_NAME, NEW_EMAIL, PHONE_NUMBER, NEW_CNPJ);
        when(repository.findById(ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.updateSeller(form));
        assertNotNull(exception);
        assertEquals("Seller not found.", exception.getMessage());
    }

    @Test
    void testFindSellerById() {
        doReturn(optionalSeller()).when(repository).findById(ID);
        SellerResponse response = service.findSellerById(ID);
        assertNotNull(response);
        verify(repository).findById(ID);
    }

    @Test
    void testFindSellerByIdNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.findSellerById(ID));
        assertNotNull(exception);
        assertEquals("Seller not found.", exception.getMessage());
        verify(repository).findById(ID);
    }

    @Test
    void testDeleteSellerById() {
        doReturn(optionalSeller()).when(repository).findById(ID);
        doNothing().when(repository).deleteById(ID);
        service.deleteSellerById(ID);
        verify(repository).findById(ID);
        verify(repository).deleteById(ID);
    }

    @Test
    void testDeleteSellerByIdNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.deleteSellerById(ID));
        assertNotNull(exception);
        assertEquals("Seller not found.", exception.getMessage());
        verify(repository).findById(ID);
        verify(repository, never()).deleteById(ID);
    }

    public Optional<Seller> optionalSeller() {
        return Optional.of(Seller.builder().build());
    }
}
