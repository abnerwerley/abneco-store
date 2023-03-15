package com.abneco.delivery.fee.service;

import com.abneco.delivery.fee.dto.EnumBrazilianRegions;
import com.abneco.delivery.fee.dto.FeeResponse;
import com.abneco.delivery.address.json.AddressTO;
import com.abneco.delivery.address.service.AddressService;
import com.abneco.delivery.exception.RequestException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class FeeService {

    @Autowired
    private AddressService addressService;

    public FeeResponse generateResponse(String cep) {
        try {
            if (cep != null) {
                AddressTO address = addressService.getAddressTemplate(cep);
                verifyRegion(address.getUf());
                return new FeeResponse(address, getFeeByZone(address.getUf()));
            }
            throw new RequestException("Cep is mandatory.");
        } catch (RequestException e) {
            log.error(e.getMessage());
            throw new RequestException(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RequestException("Could not calculate delivery fee for cep: " + cep);
        }
    }

    public String verifyRegion(String state) {
        for (EnumBrazilianRegions zone : EnumBrazilianRegions.values()) {
            if (zone.hasState(state)) {
                return zone.name();
            }
        }
        throw new NoSuchElementException("State isn't from Brazil.");
    }

    public BigDecimal getFeeByZone(String state) {
        for (EnumBrazilianRegions zone : EnumBrazilianRegions.values()) {
            if (zone.hasState(state)) {
                return zone.getFee();
            }
        }
        throw new NoSuchElementException("State isn't from Brazil.");
    }
}
