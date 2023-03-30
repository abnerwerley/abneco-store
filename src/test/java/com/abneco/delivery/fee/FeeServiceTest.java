package com.abneco.delivery.fee;

import com.abneco.delivery.address.json.AddressTO;
import com.abneco.delivery.address.service.AddressService;
import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.fee.dto.EnumBrazilianRegions;
import com.abneco.delivery.fee.service.FeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class FeeServiceTest {

    @InjectMocks
    private FeeService service;

    @Mock
    private AddressService addressService;

    public static final String CEP = "0123456";
    public static final String LOGRADOURO = "Rua exemplo";
    public static final String COMPLEMENTO = "Casa x";
    public static final String BAIRRO = "Jd. Bairro";
    public static final String LOCALIDADE = "Cidadezinha";

    public static final String SP = "SP";
    public static final String AC = "AC";
    public static final String AL = "AL";
    public static final String DF = "DF";
    public static final String PR = "PR";
    public static final String NOT_A_STATE = "bla bla bla";

    public static final BigDecimal NORTE_FEE = new BigDecimal("20.83");
    public static final BigDecimal NORDESTE_FEE = new BigDecimal("15.98");
    public static final BigDecimal CENTRO_OESTE_FEE = new BigDecimal("12.50");
    public static final BigDecimal SUDESTE_FEE = new BigDecimal("7.85");
    public static final BigDecimal SUL_FEE = new BigDecimal("17.30");

    @Test
    void testGenerateResponse() {
        doReturn(getAddressTo(SP)).when(addressService).getAddressTemplate(CEP);
        addressService.getAddressTemplate(CEP);
        BigDecimal sp = service.generateResponse(CEP);
        assertNotNull(sp);
        assertEquals(SUDESTE_FEE, sp);

        doReturn(getAddressTo(AL)).when(addressService).getAddressTemplate(CEP);
        BigDecimal al = service.generateResponse(CEP);
        assertNotNull(al);
        assertEquals(NORDESTE_FEE, al);

        doReturn(getAddressTo(DF)).when(addressService).getAddressTemplate(CEP);
        BigDecimal df = service.generateResponse(CEP);
        assertNotNull(df);
        assertEquals(CENTRO_OESTE_FEE, df);

        doReturn(getAddressTo(PR)).when(addressService).getAddressTemplate(CEP);
        BigDecimal pr = service.generateResponse(CEP);
        assertNotNull(pr);
        assertEquals(SUL_FEE, pr);

        doReturn(getAddressTo(AC)).when(addressService).getAddressTemplate(CEP);
        BigDecimal ac = service.generateResponse(CEP);
        assertNotNull(ac);
        assertEquals(NORTE_FEE, ac);

        Exception exception = Assertions.assertThrows(RequestException.class, () -> service.generateResponse(null));
        assertNotNull(exception);
        assertEquals("Cep is mandatory.", exception.getMessage());

    }

    @Test
    void testVerifyZone() {
        String sp = service.verifyRegion(SP);
        assertNotNull(sp);
        assertEquals(EnumBrazilianRegions.SUDESTE.toString(), sp);

        String ac = service.verifyRegion(AC);
        assertNotNull(ac);
        assertEquals(EnumBrazilianRegions.NORTE.toString(), ac);

        String al = service.verifyRegion(AL);
        assertNotNull(al);
        assertEquals(EnumBrazilianRegions.NORDESTE.toString(), al);

        String df = service.verifyRegion(DF);
        assertNotNull(df);
        assertEquals(EnumBrazilianRegions.CENTRO_OESTE.toString(), df);

        String pr = service.verifyRegion(PR);
        assertNotNull(pr);
        assertEquals(EnumBrazilianRegions.SUL.toString(), pr);

        Exception notAState = Assertions.assertThrows(NoSuchElementException.class, () -> service.verifyRegion(NOT_A_STATE));
        assertNotNull(notAState);
        assertEquals("State isn't from Brazil.", notAState.getMessage());
    }

    @Test
    void testGetFeeByZone() {
        BigDecimal sp = service.getFeeByZone(SP);
        assertNotNull(sp);
        assertEquals(SUDESTE_FEE, sp);

        BigDecimal ac = service.getFeeByZone(AC);
        assertNotNull(ac);
        assertEquals(NORTE_FEE, ac);

        BigDecimal al = service.getFeeByZone(AL);
        assertNotNull(al);
        assertEquals(NORDESTE_FEE, al);

        BigDecimal df = service.getFeeByZone(DF);
        assertNotNull(df);
        assertEquals(CENTRO_OESTE_FEE, df);

        BigDecimal pr = service.getFeeByZone(PR);
        assertNotNull(pr);
        assertEquals(SUL_FEE, pr);

        Exception notAState = Assertions.assertThrows(NoSuchElementException.class, () -> service.getFeeByZone(NOT_A_STATE));
        assertNotNull(notAState);
        assertEquals("State isn't from Brazil.", notAState.getMessage());
    }

    public AddressTO getAddressTo(String uf) {
        return AddressTO.builder()
                .cep(CEP)
                .logradouro(LOGRADOURO)
                .complemento(COMPLEMENTO)
                .bairro(BAIRRO)
                .localidade(LOCALIDADE)
                .uf(uf)
                .build();
    }
}
