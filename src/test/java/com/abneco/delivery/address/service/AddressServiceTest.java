package com.abneco.delivery.address.service;

import com.abneco.delivery.address.entity.Address;
import com.abneco.delivery.address.json.AddressForm;
import com.abneco.delivery.address.json.AddressResponse;
import com.abneco.delivery.address.json.AddressTO;
import com.abneco.delivery.address.json.AddressUpdateForm;
import com.abneco.delivery.address.repository.AddressRepository;
import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.user.entity.JuridicalPerson;
import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @InjectMocks
    private AddressService service;

    @Mock
    private AddressRepository repository;

    @Mock
    private UserRepository userRepository;

    public static final String SELLER_ID = "alkdbmncvpasidupqowieursdasd";
    public static final String CEP = "04555-000";
    private static final String NEW_CEP = "69312349";
    public static final String NOVO_COMPLEMENTO = "lado impar";
    public static final Integer NOVO_NUMERO = 15;

    public static final String CEP_NUMBERS = "04555000";
    public static final String CEP_9 = "123456789";
    public static final String CEP_LETTER = "04555A00";
    public static final String COMPLEMENTO = "";
    public static final Integer NUMERO = 123;
    public static final JuridicalPerson JURIDICAL_PERSON = new JuridicalPerson("email.@gmail.com", "12345678123456", "Abneco Delivery", "12345678", 11908765132L, false);
    public static final Seller SELLER = new Seller("kasdjlfkajsçdlkfjalçkdjfalkdjf", JURIDICAL_PERSON, "", "");
    public static final AddressForm ADDRESS_FORM = new AddressForm("kasdjlfkajsçdlkfjalçkdjfalkdjf", "12345678", "", 24);
    public static final Address ADDRESS = new Address(SELLER, ADDRESS_FORM, "rua tal", "jardim do meu endereço", "cidade exemplo", "RJ");
    public static final String ADDRESS_ID = "calskdjfalkjdfclakncldjaojidfasdflj";
    public static final Seller SELLER_WITH_ADDRESS = new Seller("kasdjlfkajsçdlkfjalçkdjfalkdjf", JURIDICAL_PERSON, "", "", ADDRESS);


    @Test
    void testGetAddress() {
        RestTemplate restTemplate = new RestTemplate();
        AddressService service = new AddressService(restTemplate);
        AddressTO response = service.getAddressTemplate(CEP);
        assertNotNull(response);
        assertEquals("SP", response.getUf());
    }

    @Test
    void testGetAddressCepOnlyNumbers() {
        RestTemplate restTemplate = new RestTemplate();
        AddressService service = new AddressService(restTemplate);
        AddressTO response = service.getAddressTemplate(CEP_NUMBERS);
        assertNotNull(response);
        assertEquals("SP", response.getUf());
    }

    @Test
    void testGetAddressCepEmptyString() {
        RestTemplate restTemplate = new RestTemplate();
        AddressService service = new AddressService(restTemplate);
        Exception exception = assertThrows(RequestException.class, () -> service.getAddressTemplate(""));
        assertEquals("Please verify if cep has 8 numbers, and numbers only.", exception.getMessage());
    }

    @Test
    void testGetAddressCepNineNumbers() {
        RestTemplate restTemplate = new RestTemplate();
        AddressService service = new AddressService(restTemplate);
        Exception exception = assertThrows(RequestException.class, () -> service.getAddressTemplate(CEP_9));
        assertEquals("Please verify if cep has 8 numbers, and numbers only.", exception.getMessage());
    }

    @Test
    void testGetAddressCepWithLetterAmid() {
        RestTemplate restTemplate = new RestTemplate();
        AddressService service = new AddressService(restTemplate);
        Exception exception = assertThrows(RequestException.class, () -> service.getAddressTemplate(CEP_LETTER));
        assertEquals("Please verify if cep has 8 numbers, and numbers only.", exception.getMessage());
    }

    @Test
    void testRegisterAddressByCep() {
        RestTemplate restTemplate = new RestTemplate();
        AddressService service = new AddressService(repository, userRepository, restTemplate);

        when(userRepository.findById(SELLER_ID)).thenReturn(Optional.of(new Seller()));
        AddressForm form = new AddressForm(SELLER_ID, CEP, COMPLEMENTO, NUMERO);
        service.registerAddressByCep(form);
        verify(repository).save(any(Address.class));
    }

    @Test
    void testRegisterAddressByCepRequestException() {
        RestTemplate restTemplate = new RestTemplate();
        AddressService service = new AddressService(repository, userRepository, restTemplate);
        when(userRepository.findById(SELLER_ID)).thenReturn(Optional.of(new Seller()));

        AddressForm form = new AddressForm(SELLER_ID, CEP_LETTER, COMPLEMENTO, NUMERO);
        Exception exception = assertThrows(RequestException.class, () -> service.registerAddressByCep(form));
        assertEquals("Please verify if cep has 8 numbers, and numbers only.", exception.getMessage());
        verify(repository, never()).save(any(Address.class));
    }

    @Test
    void testRegisterAddressByCepUserAlreadyHasAddress() {
        RestTemplate restTemplate = new RestTemplate();
        AddressService service = new AddressService(repository, userRepository, restTemplate);
        when(userRepository.findById(SELLER_ID)).thenReturn(Optional.of(SELLER_WITH_ADDRESS));

        AddressForm form = new AddressForm(SELLER_ID, CEP_LETTER, COMPLEMENTO, NUMERO);
        Exception exception = assertThrows(RequestException.class, () -> service.registerAddressByCep(form));
        assertEquals("User must only has one address.", exception.getMessage());
        verify(repository, never()).save(any(Address.class));
    }

    @Test
    void testRegisterAddressByCepException() {
        RestTemplate restTemplate = new RestTemplate();
        AddressService service = new AddressService(repository, userRepository, restTemplate);
        when(userRepository.findById(SELLER_ID)).thenReturn(Optional.of(new Seller()));

        AddressForm form = new AddressForm(SELLER_ID, CEP, COMPLEMENTO, NUMERO);

        when(repository.save(any(Address.class))).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.registerAddressByCep(form));
        assertEquals("Could not register address by cep.", exception.getMessage());
    }

    @Test
    void testRegisterAddressByCepLengthException() {
        RestTemplate restTemplate = new RestTemplate();
        AddressService service = new AddressService(repository, userRepository, restTemplate);
        when(userRepository.findById(SELLER_ID)).thenReturn(Optional.of(new Seller()));

        AddressForm form = new AddressForm(SELLER_ID, CEP, COMPLEMENTO, null);

        Exception exception = assertThrows(RequestException.class, () -> service.registerAddressByCep(form));
        assertEquals("Address number must not be null.", exception.getMessage());
    }

    @Test
    void testGetAllAddresses() {
        when(repository.findAll()).thenReturn(List.of(ADDRESS));
        List<AddressResponse> response = service.getAllAddresses();
        assertNotEquals(0, response.size());
        verify(repository).findAll();
    }

    @Test
    void testGetAllAddressesEmptyList() {
        when(repository.findAll()).thenReturn(List.of());
        List<AddressResponse> response = service.getAllAddresses();
        assertEquals(0, response.size());
        verify(repository).findAll();
    }

    @Test
    void testGetAllAddressesException() {
        when(repository.findAll()).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.getAllAddresses());
        assertNotNull(exception);
        assertEquals("Could not get all addresses.", exception.getMessage());
        verify(repository).findAll();
    }

    @Test
    void testUpdateAddress() {
        RestTemplate restTemplate = new RestTemplate();
        AddressService service = new AddressService(repository, userRepository, restTemplate);
        when(userRepository.findById(SELLER_ID)).thenReturn(Optional.of(SELLER));
        when(repository.findById(ADDRESS_ID)).thenReturn(Optional.of(ADDRESS));
        AddressUpdateForm updateAddressForm = new AddressUpdateForm(ADDRESS_ID, SELLER_ID, NEW_CEP, NOVO_COMPLEMENTO, NOVO_NUMERO);
        service.updateAddress(updateAddressForm);
        verify(userRepository).findById(SELLER_ID);
        verify(repository).findById(ADDRESS_ID);
        verify(repository).save(any(Address.class));
    }

    @Test
    void testUpdateAddressUserNotFound() {
        RestTemplate restTemplate = new RestTemplate();
        AddressService service = new AddressService(repository, userRepository, restTemplate);
        when(userRepository.findById(SELLER_ID)).thenReturn(Optional.empty());
        AddressUpdateForm updateAddressForm = new AddressUpdateForm(ADDRESS_ID, SELLER_ID, NEW_CEP, NOVO_COMPLEMENTO, NOVO_NUMERO);
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.updateAddress(updateAddressForm));
        assertNotNull(exception);
        assertEquals("User not found.", exception.getMessage());
        verify(userRepository).findById(SELLER_ID);
        verify(repository, never()).save(any(Address.class));
    }

    @Test
    void testUpdateAddressSellerNotFound() {
        RestTemplate restTemplate = new RestTemplate();
        AddressService service = new AddressService(repository, userRepository, restTemplate);
        when(userRepository.findById(SELLER_ID)).thenReturn(Optional.of(SELLER));
        when(repository.findById(ADDRESS_ID)).thenReturn(Optional.empty());

        AddressUpdateForm updateAddressForm = new AddressUpdateForm(ADDRESS_ID, SELLER_ID, NEW_CEP, NOVO_COMPLEMENTO, NOVO_NUMERO);
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.updateAddress(updateAddressForm));
        assertNotNull(exception);
        assertEquals("Address not found.", exception.getMessage());
        verify(repository).findById(ADDRESS_ID);
        verify(repository, never()).save(any(Address.class));
    }

    @Test
    void testUpdateAddressRequestException() {
        RestTemplate restTemplate = new RestTemplate();
        AddressService service = new AddressService(repository, userRepository, restTemplate);
        AddressUpdateForm updateAddressForm = new AddressUpdateForm(ADDRESS_ID, SELLER_ID, NEW_CEP, NOVO_COMPLEMENTO, null);

        when(userRepository.findById(SELLER_ID)).thenReturn(Optional.of(SELLER));
        when(repository.findById(ADDRESS_ID)).thenReturn(Optional.of(ADDRESS));
        Exception exception = assertThrows(RequestException.class, () -> service.updateAddress(updateAddressForm));
        assertNotNull(exception);
        assertEquals("Address number must not be null.", exception.getMessage());
        verify(userRepository).findById(SELLER_ID);
        verify(repository).findById(ADDRESS_ID);
        verify(repository, never()).save(any(Address.class));
    }

    @Test
    void testUpdateAddressException() {
        AddressUpdateForm updateAddressForm = new AddressUpdateForm(ADDRESS_ID, SELLER_ID, NEW_CEP, NOVO_COMPLEMENTO, NOVO_NUMERO);

        when(userRepository.findById(SELLER_ID)).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.updateAddress(updateAddressForm));
        assertNotNull(exception);
        assertEquals("Could not update address.", exception.getMessage());
        verify(repository, never()).save(any(Address.class));
    }

    @Test
    void testDeleteAddressById() {
        when(repository.findById(ADDRESS_ID)).thenReturn(Optional.of(new Address()));
        service.deleteAddressById(ADDRESS_ID);
        verify(repository).findById(ADDRESS_ID);
        verify(repository).delete(any(Address.class));
    }

    @Test
    void testDeleteAddressByIdNotFound() {
        when(repository.findById(ADDRESS_ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.deleteAddressById(ADDRESS_ID));
        assertNotNull(exception);
        assertEquals("Address not found.", exception.getMessage());
        verify(repository).findById(ADDRESS_ID);
        verify(repository, never()).delete(any(Address.class));
    }

    @Test
    void testDeleteAddressByIdException() {
        when(repository.findById(ADDRESS_ID)).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.deleteAddressById(ADDRESS_ID));
        assertNotNull(exception);
        assertEquals("Could not delete Address.", exception.getMessage());
        verify(repository).findById(ADDRESS_ID);
        verify(repository, never()).delete(any(Address.class));
    }
}
