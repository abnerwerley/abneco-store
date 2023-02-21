package com.abneco.delivery.addressService;

import com.abneco.delivery.address.dto.AddressTO;
import com.abneco.delivery.address.service.AddressService;
import com.abneco.delivery.exception.RequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AddressServiceTest {

    public static final String CEP = "04555-000";
    public static final String CEP_NUMBERS = "04555000";
    public static final String CEP_9 = "123456789";
    public static final String CEP_LETTER = "04555A00";

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
        Exception exception = Assertions.assertThrows(RequestException.class, () -> service.getAddressTemplate(""));
        assertNotNull(exception);
        assertEquals("Please verify if cep has 8 numbers, and numbers only.", exception.getMessage());
    }

    @Test
    void testGetAddressCepNineNumbers() {
        RestTemplate restTemplate = new RestTemplate();
        AddressService service = new AddressService(restTemplate);
        Exception exception = Assertions.assertThrows(RequestException.class, () -> service.getAddressTemplate(CEP_9));
        assertNotNull(exception);
        assertEquals("Please verify if cep has 8 numbers, and numbers only.", exception.getMessage());
    }

    @Test
    void testGetAddressCepWithLetterAmid() {
        RestTemplate restTemplate = new RestTemplate();
        AddressService service = new AddressService(restTemplate);
        Exception exception = Assertions.assertThrows(RequestException.class, () -> service.getAddressTemplate(CEP_LETTER));
        assertNotNull(exception);
        assertEquals("Please verify if cep has 8 numbers, and numbers only.", exception.getMessage());
    }
}
