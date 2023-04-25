package com.abneco.store.user.service;

import com.abneco.store.exception.RequestException;
import com.abneco.store.exception.ResourceNotFoundException;
import com.abneco.store.user.entity.Buyer;
import com.abneco.store.user.entity.NaturalPerson;
import com.abneco.store.user.json.buyer.BuyerForm;
import com.abneco.store.user.json.buyer.BuyerResponse;
import com.abneco.store.user.json.buyer.BuyerUpdateForm;
import com.abneco.store.user.repository.BuyerRepository;
import com.abneco.store.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static com.abneco.store.user.service.SellerServiceTest.NEW_EMAIL;
import static com.abneco.store.user.service.SellerServiceTest.NEW_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuyerServiceTest {

    @InjectMocks
    private BuyerService service;

    @Mock
    private BuyerRepository repository;

    @Mock
    private UserRepository userRepository;


    public static final String ID = "i091287409dkfajkdhfjkagdkfj";
    public static final String NAME = "Name";
    public static final String EMAIL = "string@email.com";
    public static final String PASSWORD = "12345678";
    public static final Long PHONE_NUMBER = 1112345678L;
    public static final String CPF = "12312312312";
    public static final String SHORT_CPF = "1231231231";
    public static final NaturalPerson NATURAL_PERSON = new NaturalPerson(EMAIL, CPF, NAME, PASSWORD, PHONE_NUMBER, false);
    public static final Buyer BUYER = new Buyer(NATURAL_PERSON, "", "");
    private static final Long NEW_PHONE_NUMBER = 11912344321L;
    private static final String NEW_CPF = "21312312312";
    public static final BuyerUpdateForm UPDATE_FORM = new BuyerUpdateForm(ID, NEW_NAME, NEW_EMAIL, NEW_PHONE_NUMBER, NEW_CPF);
    public static final BuyerUpdateForm UPDATE_FORM_SHORT_CPF = new BuyerUpdateForm(ID, NEW_NAME, NEW_EMAIL, NEW_PHONE_NUMBER, "12349876");

    @Test
    void testRegister() {
        BuyerForm form = new BuyerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CPF);
        when(userRepository.findUserByEmail(form.getEmail())).thenReturn(Optional.empty());

        service.register(form);
        verify(userRepository).findUserByEmail(EMAIL);
        verify(repository).save(any(Buyer.class));
    }

    @Test
    void testRegisterBuyerEmailAlreadyInUse() {
        BuyerForm form = new BuyerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CPF);
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(new Buyer()));

        Exception exception = assertThrows(RequestException.class, () -> service.register(form));
        assertNotNull(exception);
        assertEquals("Email already in use.", exception.getMessage());
        verify(userRepository).findUserByEmail(EMAIL);
        verify(repository, never()).save(any(Buyer.class));
    }

    @Test
    void testRegisterBuyerShortCpf() {
        BuyerForm form = new BuyerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, SHORT_CPF);
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RequestException.class, () -> service.register(form));
        assertNotNull(exception);
        assertEquals("Cpf must have 11 numbers, and numbers only.", exception.getMessage());
        verify(userRepository).findUserByEmail(EMAIL);
        verify(repository, never()).save(any(Buyer.class));
    }

    @Test
    void testRegisterBuyerException() {
        BuyerForm form = new BuyerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, SHORT_CPF);
        when(userRepository.findUserByEmail(EMAIL)).thenThrow(RuntimeException.class);

        Exception exception = assertThrows(RequestException.class, () -> service.register(form));
        assertNotNull(exception);
        assertEquals("Could not register user.", exception.getMessage());
        verify(userRepository).findUserByEmail(EMAIL);
        verify(repository, never()).save(any(Buyer.class));
    }

    @Test
    void testGetAllBuyers() {
        when(repository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"))).thenReturn(List.of(new Buyer()));
        List<BuyerResponse> response = service.findAllBuyers();
        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void testGetAllBuyersException() {
        when(repository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"))).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.findAllBuyers());
        assertNotNull(exception);
        assertEquals("Could not get all buyers.", exception.getMessage());
    }

    @Test
    void testFindBuyerById() {
        when(repository.findById(ID)).thenReturn(Optional.of(BUYER));
        BuyerResponse response = service.findBuyerById(ID);
        assertNotNull(response);
        assertEquals(BUYER.getId(), response.getId());
        assertEquals(BUYER.getEmail(), response.getEmail());
        assertEquals(BUYER.getCpf(), response.getCpf());
        assertEquals(BUYER.getName(), response.getName());
        assertEquals(BUYER.getPhoneNumber(), response.getPhoneNumber());
        verify(repository).findById(ID);
    }

    @Test
    void testFindBuyerByIdNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.findBuyerById(ID));
        assertNotNull(exception);
        assertEquals("Buyer not found.", exception.getMessage());
        verify(repository).findById(ID);
    }

    @Test
    void testFindBuyerByIdException() {
        when(repository.findById(ID)).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.findBuyerById(ID));
        assertNotNull(exception);
        assertEquals("Could not find buyer by id.", exception.getMessage());
        verify(repository).findById(ID);
    }

    @Test
    void testUpdateBuyer() {
        when(repository.findById(ID)).thenReturn(Optional.of(BUYER));
        service.updateBuyer(UPDATE_FORM);
        verify(repository).findById(ID);
        verify(repository).save(any(Buyer.class));
    }

    @Test
    void testUpdateBuyerNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.updateBuyer(UPDATE_FORM));
        assertNotNull(exception);
        assertEquals("Buyer not found.", exception.getMessage());
        verify(repository).findById(ID);
        verify(repository, never()).save(any(Buyer.class));
    }

    @Test
    void testUpdateBuyerRequestException() {
        when(repository.findById(ID)).thenReturn(Optional.of(BUYER));
        Exception exception = assertThrows(RequestException.class, () -> service.updateBuyer(UPDATE_FORM_SHORT_CPF));
        assertNotNull(exception);
        assertEquals("Cpf must have 11 numbers, and numbers only.", exception.getMessage());
        verify(repository).findById(ID);
        verify(repository, never()).save(any(Buyer.class));
    }

    @Test
    void testUpdateBuyerException() {
        when(repository.findById(ID)).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.updateBuyer(UPDATE_FORM));
        assertNotNull(exception);
        assertEquals("Could not update buyer.", exception.getMessage());
        verify(repository).findById(ID);
        verify(repository, never()).save(any(Buyer.class));
    }

    @Test
    void testDeleteBuyerById() {
        when(repository.findById(ID)).thenReturn(Optional.of(BUYER));
        service.deleteBuyerById(ID);
        verify(repository).findById(ID);
    }

    @Test
    void testDeleteBuyerByIdNoFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.deleteBuyerById(ID));
        assertNotNull(exception);
        assertEquals("Buyer not found.", exception.getMessage());
        verify(repository).findById(ID);
        verify(repository, never()).delete(any(Buyer.class));
    }

    @Test
    void testDeleteBuyerByIdException() {
        when(repository.findById(ID)).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.deleteBuyerById(ID));
        assertNotNull(exception);
        assertEquals("Could not delete buyer with id: " + ID, exception.getMessage());
        verify(repository).findById(ID);
        verify(repository, never()).delete(any(Buyer.class));
    }
}
