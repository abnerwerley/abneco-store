package com.abneco.store.external.viacep.service;

import com.abneco.store.address.json.AddressTO;
import com.abneco.store.exception.RequestException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@AllArgsConstructor
public class ViacepService {

    @Autowired
    private RestTemplate restTemplate;

    public AddressTO getAddressTemplate(String cep) {
        try {
            String url = "https://viacep.com.br/ws/" + cep + "/json/";
            return restTemplate.getForEntity(url, AddressTO.class).getBody();

        } catch (Exception e) {

            log.error("Please verify if cep has 8 numbers, and numbers only.");
            throw new RequestException("Please verify if cep has 8 numbers, and numbers only.");
        }
    }
}
