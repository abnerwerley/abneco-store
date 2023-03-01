package com.abneco.delivery.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class NameFormatterTest {

    public static final String JOHN_SMITH = "john smith";
    public static final String MARIA_JONES_JACKSON = "maria jones jackson";
    public static final String JOAO_RODRIGUES_SANTANA = "joao rodrigues santana";

    @Test
    void testReplaceFirstLetterToCapitalLetter() {
        String johnSmith = NameFormatter.formatToCapitalLetter(JOHN_SMITH);
        assertEquals("John Smith", johnSmith);

        String mariaJonesJackson = NameFormatter.formatToCapitalLetter(MARIA_JONES_JACKSON);
        assertEquals("Maria Jones Jackson", mariaJonesJackson);

        String joaoRodriguesSantana = NameFormatter.formatToCapitalLetter(JOAO_RODRIGUES_SANTANA);
        assertEquals("Joao Rodrigues Santana", joaoRodriguesSantana);
    }
}
