package com.abneco.delivery.user.service;

import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.user.entity.Buyer;
import com.abneco.delivery.user.json.buyer.BuyerForm;
import com.abneco.delivery.user.json.buyer.BuyerResponse;
import com.abneco.delivery.user.repository.BuyerRepository;
import com.abneco.delivery.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuyerServiceTest {

    @InjectMocks
    private BuyerService service;

    @Mock
    private BuyerRepository repository;

    @Mock
    private UserRepository userRepository;

    public static final String NAME = "Name";
    public static final String EMAIL = "string@email.com";
    public static final String PASSWORD = "12345678";
    public static final Long PHONE_NUMBER = 1112345678L;
    public static final String CPF = "12312312312";
    public static final String SHORT_CPF = "1231231231";

    @Test
    void testRegisterBuyer() {
        BuyerForm form = new BuyerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CPF);
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());

        service.registerBuyer(form);
        verify(userRepository).findUserByEmail(EMAIL);
        verify(repository).save(any(Buyer.class));
    }

    @Test
    void testRegisterBuyerEmailAlreadyInUse() {
        BuyerForm form = new BuyerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CPF);
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(new Buyer()));

        Exception exception = assertThrows(RequestException.class, () -> service.registerBuyer(form));
        assertNotNull(exception);
        assertEquals("Email already in use.", exception.getMessage());
        verify(userRepository).findUserByEmail(EMAIL);
        verify(repository, never()).save(any(Buyer.class));
    }

    @Test
    void testRegisterBuyerShortCpf() {
        BuyerForm form = new BuyerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, SHORT_CPF);
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RequestException.class, () -> service.registerBuyer(form));
        assertNotNull(exception);
        assertEquals("Cpf must have 11 numbers, and numbers only.", exception.getMessage());
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

}
