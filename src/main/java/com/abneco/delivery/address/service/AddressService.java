package com.abneco.delivery.address.service;

import com.abneco.delivery.address.dto.AddressTO;
import com.abneco.delivery.exception.RequestException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class AddressService {

    @Autowired
    private RestTemplate restTemplate;

    public AddressTO getAddressTemplate(String cep) {
        try {
            String url = "https://viacep.com.br/ws/" + cep + "/json/";
            ResponseEntity<AddressTO> response = restTemplate.getForEntity(url, AddressTO.class);
            return response.getBody();

        } catch (Exception e) {

            log.error("Please verify if cep has 8 numbers, and numbers only.");
            throw new RequestException("Please verify if cep has 8 numbers, and numbers only.");
        }
    }
}