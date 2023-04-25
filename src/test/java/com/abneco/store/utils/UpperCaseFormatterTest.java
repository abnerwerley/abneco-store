package com.abneco.store.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UpperCaseFormatterTest {

    @Test
    void testReplaceFirstLetterToCapitalLetter() {
        String johnSmith = UpperCaseFormatter.formatToCapitalLetter("john smith");
        assertEquals("John Smith", johnSmith);

        String mariaJonesJackson = UpperCaseFormatter.formatToCapitalLetter("maria jones jackson");
        assertEquals("Maria Jones Jackson", mariaJonesJackson);

        String joaoRodriguesSantana = UpperCaseFormatter.formatToCapitalLetter("joao rodrigues santana");
        assertEquals("Joao Rodrigues Santana", joaoRodriguesSantana);

        String number = UpperCaseFormatter.formatToCapitalLetter("1");
        assertEquals("1", number);

        String nameWithDot = UpperCaseFormatter.formatToCapitalLetter("name.silva");
        assertEquals("Name.silva", nameWithDot);

        String nameWithSpaceAndDot = UpperCaseFormatter.formatToCapitalLetter("name .silva");
        assertEquals("Name .silva", nameWithSpaceAndDot);

        String justDot = UpperCaseFormatter.formatToCapitalLetter(".");
        assertEquals(".", justDot);
    }
}
