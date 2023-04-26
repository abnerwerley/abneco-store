package com.abneco.store.fee.service;

import com.abneco.store.address.json.AddressTO;
import com.abneco.store.exception.RequestException;
import com.abneco.store.external.viacep.service.ViacepService;
import com.abneco.store.fee.dto.EnumBrazilianRegions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
@Slf4j
@AllArgsConstructor
public class FeeService {

    @Autowired
    private ViacepService viacepService;

    public BigDecimal calculateFee(String cep) {
        try {
            if (cep != null) {
                AddressTO address = viacepService.getAddressTemplate(cep);
                return getFeeByZone(address.getUf());
            }
            throw new RequestException("Cep is mandatory.");

        } catch (RequestException e) {
            log.error(e.getMessage());
            throw new RequestException(e.getMessage());

        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            throw new NoSuchElementException(e.getMessage());

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RequestException("Could not calculate delivery fee for cep: " + cep);
        }
    }

    private BigDecimal getFeeByZone(String state) {
        for (EnumBrazilianRegions zone : EnumBrazilianRegions.values()) {
            if (zone.hasState(state)) {
                return zone.getFee();
            }
        }
        throw new NoSuchElementException("State isn't from Brazil.");
    }
}
