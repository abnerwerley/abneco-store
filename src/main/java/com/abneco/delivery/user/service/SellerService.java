package com.abneco.delivery.user.service;

import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.entity.mapper.SellerMapper;
import com.abneco.delivery.user.json.SellerForm;
import com.abneco.delivery.user.json.SellerResponse;
import com.abneco.delivery.user.json.SellerUpdateForm;
import com.abneco.delivery.user.json.mapper.SellerResponseMapper;
import com.abneco.delivery.user.repository.SellerRepository;
import com.abneco.delivery.utils.DateFormatter;
import com.abneco.delivery.utils.NameFormatter;
import com.abneco.delivery.utils.ValidateEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class SellerService {

    @Autowired
    private SellerRepository repository;

    private String passwordEncryptor(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public static final String SELLER_NOT_FOUND = "Seller not found.";

    public void registerSeller(SellerForm form) {
        try {
            ValidateEmail.validateEmail(form.getEmail());
            Optional<Seller> optionalUser = repository.findByEmail(form.getEmail());
            if (optionalUser.isPresent()) {
                throw new RequestException("Email already in use.");
            }
            save(SellerMapper.fromFormToSellerEntity(form), form);
        } catch (RequestException e) {
            log.error(e.getMessage());
            throw new RequestException(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RequestException("Could not register seller.");
        }
    }

    public SellerResponse updateSeller(SellerUpdateForm form) {
        try {
            Optional<Seller> optionalUser = repository.findById(form.getId());
            if (optionalUser.isEmpty()) {
                throw new ResourceNotFoundException(SELLER_NOT_FOUND);
            }
            Seller seller = optionalUser.get();
            seller.setName(NameFormatter.formatToCapitalLetter(form.getName()));
            seller.setEmail(form.getEmail());
            seller.setPhoneNumber(form.getPhoneNumber());
            seller.setCnpj(form.getCnpj());
            seller.setUpdatedAt(DateFormatter.formatNow());
            ValidateEmail.validateEmail(form.getEmail());
            save(seller, form);
            return SellerResponseMapper.fromEntityToResponse(seller);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RequestException("Could not update seller.");
        }
    }

    public SellerResponse findSellerById(String id) {
        try {
            Optional<Seller> optionalSeller = repository.findById(id);
            if (optionalSeller.isEmpty()) {
                throw new ResourceNotFoundException(SELLER_NOT_FOUND);
            }
            return SellerResponseMapper.fromEntityToResponse(optionalSeller.get());
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RequestException("Could not find seller by id: " + id);
        }
    }

    public void deleteSellerById(String id) {
        try {
            Optional<Seller> optionalSeller = repository.findById(id);
            if (optionalSeller.isEmpty()) {
                throw new ResourceNotFoundException(SELLER_NOT_FOUND);
            }
            repository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RequestException("Could not find seller by id: " + id);
        }
    }

    private void save(Seller seller, SellerForm form) {
        if (seller.getCnpj() == null || seller.getCnpj().length() != 14) {
            throw new RequestException("Cnpj must have 14 numbers, and numbers only.");
        }
        if (seller.getName() == null || seller.getName().length() < 3) {
            throw new RequestException("Name must be neither null nor shorter than 3.");
        }
        if (seller.getPassword().length() < 8) {
            seller.setPassword(passwordEncryptor(form.getPassword()));
            throw new RequestException("Password must be at least 8 char long.");
        }
        repository.save(seller);
    }
    private void save(Seller seller, SellerUpdateForm form) {
        if (form.getCnpj() == null || form.getCnpj().length() != 14) {
            throw new RequestException("Cnpj must have 14 numbers, and numbers only.");
        }
        if (form.getName() == null || form.getName().length() < 3) {
            throw new RequestException("Name must be neither null nor shorter than 3.");
        }
        repository.save(seller);
    }
}
