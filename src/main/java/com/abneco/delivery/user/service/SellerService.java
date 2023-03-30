package com.abneco.delivery.user.service;

import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.json.seller.SellerForm;
import com.abneco.delivery.user.json.seller.SellerResponse;
import com.abneco.delivery.user.json.seller.UpdateSellerForm;
import com.abneco.delivery.user.repository.SellerRepository;
import com.abneco.delivery.user.repository.UserRepository;
import com.abneco.delivery.user.utils.ValidateSeller;
import com.abneco.delivery.utils.DateFormatter;
import com.abneco.delivery.utils.UpperCaseFormatter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class SellerService {

    @Autowired
    private SellerRepository repository;

    @Autowired
    private UserRepository userRepository;

    public static final String SELLER_NOT_FOUND = "Seller not found.";

    private static String passwordEncryptor(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public void registerSeller(SellerForm form) {
        try {
            userRepository.findUserByEmail(form.getEmail())
                    .ifPresent(user -> {
                        throw new RequestException("Email already in use.");
                    });

            repository.findByCnpj(form.getCnpj())
                    .ifPresent(sellerCnpj -> {
                        throw new RequestException("Cnpj already in use.");
                    });

            save(form);
        } catch (RequestException e) {
            log.error(e.getMessage());
            throw new RequestException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RequestException("Could not register seller.");
        }
    }

    public void updateSeller(UpdateSellerForm form) {
        try {
            Seller seller = repository.findById(form.getId()).orElseThrow(() -> new ResourceNotFoundException(SELLER_NOT_FOUND));
            userRepository.findUserByEmail(form.getEmail())
                    .ifPresent(user -> {
                        throw new RequestException("Email already in use.");
                    });

            repository.findByCnpj(form.getCnpj())
                    .ifPresent(sellerCnpj -> {
                        throw new RequestException("Cnpj already in use.");
                    });

            seller.setName(UpperCaseFormatter.formatToCapitalLetter(form.getName()));
            seller.setEmail(form.getEmail());
            seller.setPhoneNumber(form.getPhoneNumber());
            seller.setCnpj(form.getCnpj());
            seller.setUpdatedAt(DateFormatter.formatNow());
            save(seller, form);

        } catch (ResourceNotFoundException e) {
            log.error("Seller not found: " + e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (RequestException e) {
            log.error(e.getMessage());
            throw new RequestException(e.getMessage());

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RequestException("Could not update seller.");
        }
    }

    public SellerResponse findSellerById(String id) {
        try {
            Seller seller = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(SELLER_NOT_FOUND));
            return seller.toResponse();
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RequestException("Could not find seller by id: " + id);
        }
    }

    public List<SellerResponse> findAllSellers() {
        try {
            List<Seller> sellers = repository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"));
            return sellers.stream()
                    .map(Seller::toResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Could not get all sellers. " + e.getMessage());
            throw new RequestException("Could not get all sellers.");
        }
    }

    public void deleteSellerById(String id) {
        try {
            Seller seller = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(SELLER_NOT_FOUND));
            repository.delete(seller);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RequestException("Could not delete seller with id: " + id);
        }
    }

    private void save(SellerForm form) {
        ValidateSeller.validateSeller(form);
        Seller seller = form.toEntity();
        seller.setPassword(passwordEncryptor(form.getPassword()));
        repository.save(seller);
    }

    private void save(Seller seller, UpdateSellerForm form) {
        ValidateSeller.validateSeller(form);
        repository.save(seller);
    }
}
