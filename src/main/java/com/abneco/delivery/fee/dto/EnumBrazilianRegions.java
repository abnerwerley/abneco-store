package com.abneco.delivery.fee.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Getter
public enum EnumBrazilianRegions {

    NORTE(Arrays.asList("AC", "AP", "AM", "PA", "RO", "RR", "TO"), new BigDecimal("20.83")),
    NORDESTE(Arrays.asList("AL", "BA", "CE", "MA", "PB", "PE", "PI", "RN", "SE"), new BigDecimal("15.98")),
    CENTRO_OESTE(Arrays.asList("DF", "GO", "MT", "MS"), new BigDecimal("12.50")),
    SUDESTE(Arrays.asList("SP", "RJ", "MG", "ES"), new BigDecimal("7.85")),
    SUL(Arrays.asList("PR", "SC", "RS"), new BigDecimal("17.30"));

    private final List<String> states;
    private final BigDecimal fee;

    EnumBrazilianRegions(List<String> states, BigDecimal fee) {
        this.states = states;
        this.fee = fee;
    }

    public boolean hasState(String state) {
        return states.contains(state);
    }
}

