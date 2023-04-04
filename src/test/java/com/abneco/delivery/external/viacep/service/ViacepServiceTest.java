package com.abneco.delivery.external.viacep.service;

import com.abneco.delivery.address.json.AddressTO;
import com.abneco.delivery.exception.RequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ViacepServiceTest {

    @InjectMocks
    private ViacepService service;

    public static final RestTemplate restTemplate = new RestTemplate();
    public static final String CEP = "04555-000";
    public static final String CEP_NUMBERS = "04555000";
    public static final String CEP_9 = "123456789";
    public static final String CEP_LETTER = "04555A00";

    @Test
    void testGetAddress() {
        this.service = new ViacepService(restTemplate);
        AddressTO response = service.getAddressTemplate(CEP);
        assertNotNull(response);
        assertEquals("SP", response.getUf());
    }

    @Test
    void testGetAddressCepOnlyNumbers() {
        this.service = new ViacepService(restTemplate);
        AddressTO response = service.getAddressTemplate(CEP_NUMBERS);
        assertNotNull(response);
        assertEquals("SP", response.getUf());
    }

    @Test
    void testGetAddressCepEmptyString() {
        this.service = new ViacepService(restTemplate);
        Exception exception = assertThrows(RequestException.class, () -> service.getAddressTemplate(""));
        assertEquals("Please verify if cep has 8 numbers, and numbers only.", exception.getMessage());
    }

    @Test
    void testGetAddressCepNineNumbers() {
        this.service = new ViacepService(restTemplate);
        Exception exception = assertThrows(RequestException.class, () -> service.getAddressTemplate(CEP_9));
        assertEquals("Please verify if cep has 8 numbers, and numbers only.", exception.getMessage());
    }

    @Test
    void testGetAddressCepWithLetterAmid() {
        this.service = new ViacepService(restTemplate);
        Exception exception = assertThrows(RequestException.class, () -> service.getAddressTemplate(CEP_LETTER));
        assertEquals("Please verify if cep has 8 numbers, and numbers only.", exception.getMessage());
    }
}
