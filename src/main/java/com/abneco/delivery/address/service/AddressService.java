package com.abneco.delivery.address.service;

import com.abneco.delivery.address.entity.Address;
import com.abneco.delivery.address.json.AddressForm;
import com.abneco.delivery.address.json.AddressResponse;
import com.abneco.delivery.address.json.AddressTO;
import com.abneco.delivery.address.json.AddressUpdateForm;
import com.abneco.delivery.address.repository.AddressRepository;
import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.user.entity.User;
import com.abneco.delivery.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class AddressService {

    @Autowired
    private AddressRepository repository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestTemplate restTemplate;

    public AddressService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static final String ADDRESS_NUMBER_NOT_NULL_MESSAGE = "Address number must not be null.";

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
            User user = userRepository.findById(form.getUserId()).orElseThrow(() -> new RequestException("User does not exist."));
            if (user.getAddress() != null) {
                throw new RequestException("User must only has one address.");
            }
            AddressTO addressTO = getAddressTemplate(form.getCep());
            Address address = new Address();
            address.setUser(user);
            address.setCep(form.getCep());
            address.setLogradouro(addressTO.getLogradouro());
            address.setComplemento(form.getComplemento());
            address.setBairro(addressTO.getBairro());
            address.setCidade(addressTO.getLocalidade());
            address.setUf(addressTO.getUf());
            address.setNumero(form.getNumero());

            save(address);
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
            return addresses.stream()
                    .map(address -> address.toResponse(address.getUser().getId()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Could not get all addresses. " + e.getMessage());
            throw new RequestException("Could not get all addresses.");
        }
    }

    public void updateAddress(AddressUpdateForm form) {
        try {
            Optional<User> optionalUser = userRepository.findById(form.getUserId());
            if (optionalUser.isEmpty()) {
                throw new ResourceNotFoundException("User not found.");
            }
            Address address = repository.findById(form.getAddressId()).orElseThrow(() -> new ResourceNotFoundException("Address not found."));

            AddressTO searchByCep = this.getAddressTemplate(form.getCep());
            address.setCep(form.getCep());
            address.setLogradouro(searchByCep.getLogradouro());
            address.setBairro(searchByCep.getBairro());
            address.setCidade(searchByCep.getLocalidade());
            address.setUf(searchByCep.getUf());
            address.setComplemento(form.getComplemento());
            address.setNumero(form.getNumero());
            save(address);

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (RequestException e) {
            log.error(e.getMessage());
            throw new RequestException(e.getMessage());

        } catch (Exception e) {
            log.error("Could not update address. " + e.getMessage());
            throw new RequestException("Could not update address.");
        }
    }

    public void deleteAddressById(String addressId) {
        try {
            Address address = repository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException("Address not found."));
            repository.delete(address);

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (Exception e) {
            log.error("Could not delete Address. " + e.getMessage());
            throw new RequestException("Could not delete Address.");
        }

    }

    private void save(Address address) {
        //getAddressTemplate already verifies the length of a cep, and therefore, it cannot be null.
        if (address.getNumero() == null) {
            log.error(ADDRESS_NUMBER_NOT_NULL_MESSAGE);
            throw new RequestException(ADDRESS_NUMBER_NOT_NULL_MESSAGE);
        }
        repository.save(address);
    }
}