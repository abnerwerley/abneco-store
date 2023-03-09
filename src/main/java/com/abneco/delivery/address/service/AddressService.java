package com.abneco.delivery.address.service;

import com.abneco.delivery.address.dto.AddressForm;
import com.abneco.delivery.address.dto.AddressResponse;
import com.abneco.delivery.address.dto.AddressTO;
import com.abneco.delivery.address.entity.Address;
import com.abneco.delivery.address.repository.AddressRepository;
import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.repository.SellerRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class AddressService {

    @Autowired
    private AddressRepository repository;

    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private RestTemplate restTemplate;

    public AddressService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

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

    public void registerAddressByCep(AddressForm form) {
        try {
            Optional<Seller> seller = sellerRepository.findById(form.getUserId());
            if (seller.isEmpty()) {
                throw new RequestException("User does not exist.");
            }
            AddressTO addressTO = getAddressTemplate(form.getCep());
            Address address = new Address();
            address.setSeller(seller.get());
            address.setCep(form.getCep());
            address.setLogradouro(addressTO.getLogradouro());
            address.setComplemento(form.getComplemento());
            address.setBairro(addressTO.getBairro());
            address.setCidade(addressTO.getLocalidade());
            address.setUf(addressTO.getUf());
            address.setNumero(form.getNumero());

            save(address, form);
        } catch (RequestException e) {
            throw new RequestException(e.getMessage());

        } catch (Exception e) {
            log.error("Could not register address by cep. " + e.getMessage());
            throw new RequestException("Could not register address by cep.");
        }
    }

    public List<AddressResponse> getAllAddresses() {
        try {
            List<Address> addresses = repository.findAll();
            List<AddressResponse> response = new ArrayList<>();
            for (Address address : addresses) {
                AddressResponse responseTo = address.toResponse(address.getSeller().getId());
                response.add(responseTo);
            }
            return response;
        } catch (Exception e) {
            log.error("Could not get all addresses. " + e.getMessage());
            throw new RequestException("Could not get all addresses.");
        }
    }

    private void save(Address address, AddressForm form) {
        //getAddressTemplate already verifies the length of a cep, and therefore, it cannot be null.
        if (form.getNumero() == null) {
            log.error("Address number must not be null.");
            throw new RequestException("Address number must not be null.");
        }
        repository.save(address);
    }
}