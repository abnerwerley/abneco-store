package com.abneco.delivery.user.service;

import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.entity.User;
import com.abneco.delivery.user.json.seller.SellerForm;
import com.abneco.delivery.user.json.seller.SellerResponse;
import com.abneco.delivery.user.json.seller.UpdateSellerForm;
import com.abneco.delivery.user.repository.SellerRepository;
import com.abneco.delivery.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

    @Mock
    private SellerRepository repository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SellerService service = new SellerService(repository, userRepository);

    public static final String ID = "iyu230hskdf-dfoi7-462c-a47f-7afaade01517";
    public static final String NAME = "Name";
    public static final String NEW_NAME = "name";
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
        when(userRepository.findUserByEmail(form.getEmail())).thenReturn(Optional.empty());
        when(repository.findByCnpj(form.getCnpj())).thenReturn(Optional.empty());
        service.register(form);
        verify(userRepository).findUserByEmail(form.getEmail());
        verify(repository).findByCnpj(form.getCnpj());
        verify(repository).save(any(Seller.class));
    }

    @Test
    void testRegisterSellerEmailAlreadyInUse() {
        SellerForm form = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        doReturn(Optional.of(getSeller())).when(userRepository).findUserByEmail(form.getEmail());
        Exception exception = assertThrows(RequestException.class, () -> service.register(form));
        assertEquals("Email already in use.", exception.getMessage());
        verify(userRepository).findUserByEmail(form.getEmail());
        verify(repository, never()).save(Mockito.any(Seller.class));
    }

    @Test
    void testRegisterSellerCnpjAlreadyInUse() {
        SellerForm form = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        when(userRepository.findUserByEmail(form.getEmail())).thenReturn(Optional.empty());
        when(repository.findByCnpj(form.getCnpj())).thenReturn(Optional.of(new Seller()));
        Exception exception = assertThrows(RequestException.class, () -> service.register(form));
        assertEquals("Cnpj already in use.", exception.getMessage());
        verify(userRepository).findUserByEmail(form.getEmail());
        verify(repository, never()).save(Mockito.any(Seller.class));
    }

    @Test
    void testRegisterSellerWithNotAValidEmail() {
        SellerForm form = new SellerForm(NAME, EMAIL_WITHOUT_AT, PASSWORD, PHONE_NUMBER, CNPJ);
        Exception exception = assertThrows(RequestException.class, () -> service.register(form));
        assertEquals("Email has incorrect format.", exception.getMessage());
        verify(repository, never()).save(Mockito.any(Seller.class));
    }

    @Test
    void testRegisterSellerSaveValidations() {
        SellerForm shortCnpjForm = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, SHORT_CNPJ);
        when(userRepository.findUserByEmail(shortCnpjForm.getEmail())).thenReturn(Optional.empty());
        when(repository.findByCnpj(shortCnpjForm.getCnpj())).thenReturn(Optional.empty());
        Exception shortCnpj = assertThrows(RequestException.class, () -> service.register(shortCnpjForm));
        assertEquals("Cnpj must have 14 numbers, and numbers only.", shortCnpj.getMessage());

        SellerForm longCnpjForm = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, LONG_CNPJ);
        Exception longCnpj = assertThrows(RequestException.class, () -> service.register(longCnpjForm));
        assertEquals("Cnpj must have 14 numbers, and numbers only.", longCnpj.getMessage());
        verify(repository, never()).save(Mockito.any(Seller.class));

        SellerForm nullCnpjForm = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, null);
        Exception nullCnpj = assertThrows(RequestException.class, () -> service.register(nullCnpjForm));
        assertEquals("Cnpj must have 14 numbers, and numbers only.", nullCnpj.getMessage());
        verify(repository, never()).save(Mockito.any(Seller.class));

        SellerForm shortNameForm = new SellerForm(SHORT_NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        Exception shortName = assertThrows(RequestException.class, () -> service.register(shortNameForm));
        assertEquals("Name must be neither null nor shorter than 3.", shortName.getMessage());
        verify(repository, never()).save(Mockito.any(Seller.class));

        SellerForm nullNameForm = new SellerForm(SHORT_NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        Exception nullName = assertThrows(RequestException.class, () -> service.register(nullNameForm));
        assertEquals("Name must be neither null nor shorter than 3.", nullName.getMessage());
        verify(repository, never()).save(Mockito.any(Seller.class));
    }

    @Test
    void testRegisterSellerShortPassword() {
        SellerForm shortPasswordForm = new SellerForm(NAME, EMAIL, SHORT_PASSWORD, PHONE_NUMBER, CNPJ);
        Exception shortPassword = assertThrows(RequestException.class, () -> service.register(shortPasswordForm));
        assertEquals("Password must be at least 8 char long.", shortPassword.getMessage());
        verify(repository, never()).save(Mockito.any(Seller.class));
    }

    @Test
    void testRegisterSellerException() {
        SellerForm form = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        when(userRepository.findUserByEmail(EMAIL)).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.register(form));
        assertEquals("Could not register user.", exception.getMessage());
    }

    @Test
    void testUpdateSeller() {
        UpdateSellerForm form = new UpdateSellerForm(ID, NEW_NAME, NEW_EMAIL, PHONE_NUMBER, NEW_CNPJ);
        doReturn(Optional.of(getSeller())).when(repository).findById(ID);
        when(repository.findByCnpj(form.getCnpj())).thenReturn(Optional.empty());
        service.updateSeller(form);
        verify(repository).findById(ID);
        verify(repository).save(any(Seller.class));
    }

    @Test
    void testUpdateSellerSellerNotFound() {
        UpdateSellerForm form = new UpdateSellerForm(ID, NEW_NAME, NEW_EMAIL, PHONE_NUMBER, NEW_CNPJ);
        when(repository.findById(ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.updateSeller(form));
        assertEquals("Seller not found.", exception.getMessage());
    }

    @Test
    void testUpdateSellerSellerEmailAlreadyInUse() {
        UpdateSellerForm form = new UpdateSellerForm(ID, NEW_NAME, NEW_EMAIL, PHONE_NUMBER, NEW_CNPJ);
        when(repository.findById(ID)).thenReturn(Optional.of(getSeller()));
        when(userRepository.findUserByEmail(form.getEmail())).thenReturn(Optional.of(new User()));
        Exception exception = assertThrows(RequestException.class, () -> service.updateSeller(form));
        assertEquals("Email already in use.", exception.getMessage());
    }

    @Test
    void testUpdateSellerSellerCnpjAlreadyInUse() {
        UpdateSellerForm form = new UpdateSellerForm(ID, NEW_NAME, NEW_EMAIL, PHONE_NUMBER, NEW_CNPJ);
        when(repository.findById(ID)).thenReturn(Optional.of(getSeller()));
        when(repository.findByCnpj(form.getCnpj())).thenReturn(Optional.of(new Seller()));
        when(userRepository.findUserByEmail(form.getEmail())).thenReturn(Optional.empty());
        Exception exception = assertThrows(RequestException.class, () -> service.updateSeller(form));
        assertEquals("Cnpj already in use.", exception.getMessage());
    }

    @Test
    void testUpdateSellerRequestException() {
        UpdateSellerForm nullNameForm = new UpdateSellerForm(ID, SHORT_NAME, EMAIL, PHONE_NUMBER, CNPJ);
        when(repository.findById(nullNameForm.getId())).thenReturn(Optional.of(new Seller()));
        when(repository.findByCnpj(nullNameForm.getCnpj())).thenReturn(Optional.empty());
        Exception nullName = assertThrows(RequestException.class, () -> service.updateSeller(nullNameForm));
        assertEquals("Name must be neither null nor shorter than 3.", nullName.getMessage());
        verify(repository, never()).save(Mockito.any(Seller.class));
    }

    @Test
    void testUpdateSellerException() {
        UpdateSellerForm form = new UpdateSellerForm(ID, NAME, EMAIL, PHONE_NUMBER, CNPJ);
        when(repository.findById(form.getId())).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.updateSeller(form));
        assertEquals("Could not update seller.", exception.getMessage());
        verify(repository, never()).save(Mockito.any(Seller.class));
    }

    @Test
    void testFindSellerById() {
        doReturn(Optional.of(getSeller())).when(repository).findById(ID);
        SellerResponse response = service.findSellerById(ID);
        verify(repository).findById(ID);
    }

    @Test
    void testFindSellerByIdNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.findSellerById(ID));
        assertEquals("Seller not found.", exception.getMessage());
        verify(repository).findById(ID);
    }

    @Test
    void testFindSellerByIdException() {
        when(repository.findById(ID)).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.findSellerById(ID));
        assertEquals("Could not find seller by id: " + ID, exception.getMessage());
        verify(repository).findById(ID);
    }

    @Test
    void testDeleteSellerById() {
        doReturn(Optional.of(getSeller())).when(repository).findById(ID);
        service.deleteSellerById(ID);
        verify(repository).findById(ID);
        verify(repository).delete(any(Seller.class));
    }

    @Test
    void testDeleteSellerByIdNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.deleteSellerById(ID));
        assertEquals("Seller not found.", exception.getMessage());
        verify(repository).findById(ID);
        verify(repository, never()).delete(getSeller());
    }

    @Test
    void testDeleteSellerByIdException() {
        when(repository.findById(ID)).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.deleteSellerById(ID));
        assertEquals("Could not delete seller with id: " + ID, exception.getMessage());
        verify(repository).findById(ID);
        verify(repository, never()).delete(getSeller());
    }

    @Test
    void testFindAllSellers() {
        when(repository.findAll((Sort.by(Sort.Direction.DESC, "updatedAt")))).thenReturn(List.of(getSeller()));
        List<SellerResponse> response = service.findAllSellers();
        assertEquals(1, response.size());
    }

    @Test
    void testFindAllSellersException() {
        when(repository.findAll()).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.findAllSellers());
        assertEquals("Could not get all sellers.", exception.getMessage());
    }

    public Seller getSeller() {
        Seller seller = new Seller();
        seller.setId(ID);
        seller.setName(NAME);
        seller.setEmail(EMAIL);
        seller.setCnpj(CNPJ);
        seller.setEmailVerified(false);
        seller.setPassword(PASSWORD);
        seller.setCreatedAt("01/03/2023 14:47");
        return seller;
    }
}
