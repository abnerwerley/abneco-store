package com.abneco.store.fee;

import com.abneco.store.address.json.AddressTO;
import com.abneco.store.exception.RequestException;
import com.abneco.store.external.viacep.service.ViacepService;
import com.abneco.store.fee.service.FeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeeServiceTest {

    @InjectMocks
    private FeeService service;

    @Mock
    private ViacepService viacepService;

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
    void testCalculateFee() {
        doReturn(getAddressTo(SP)).when(viacepService).getAddressTemplate(CEP);
        viacepService.getAddressTemplate(CEP);
        BigDecimal sp = service.calculateFee(CEP);
        assertEquals(SUDESTE_FEE, sp);

        doReturn(getAddressTo(AL)).when(viacepService).getAddressTemplate(CEP);
        BigDecimal al = service.calculateFee(CEP);
        assertEquals(NORDESTE_FEE, al);

        doReturn(getAddressTo(DF)).when(viacepService).getAddressTemplate(CEP);
        BigDecimal df = service.calculateFee(CEP);
        assertEquals(CENTRO_OESTE_FEE, df);

        doReturn(getAddressTo(PR)).when(viacepService).getAddressTemplate(CEP);
        BigDecimal pr = service.calculateFee(CEP);
        assertEquals(SUL_FEE, pr);

        doReturn(getAddressTo(AC)).when(viacepService).getAddressTemplate(CEP);
        BigDecimal ac = service.calculateFee(CEP);
        assertEquals(NORTE_FEE, ac);

        Exception exception = assertThrows(RequestException.class, () -> service.calculateFee(null));
        assertEquals("Cep is mandatory.", exception.getMessage());

        doReturn(getAddressTo(NOT_A_STATE)).when(viacepService).getAddressTemplate(CEP);
        Exception notBrazilianRegionj = assertThrows(NoSuchElementException.class, () -> service.calculateFee(CEP));
        assertEquals("State isn't from Brazil.", notBrazilianRegionj.getMessage());
    }

    @Test
    void testCalculateFeeAddressException() {
        when(viacepService.getAddressTemplate(CEP)).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.calculateFee(CEP));
        assertEquals("Could not calculate delivery fee for cep: " + CEP, exception.getMessage());
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
