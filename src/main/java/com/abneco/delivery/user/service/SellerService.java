package com.abneco.delivery.user.service;

import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.entity.User;
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
import java.util.Optional;
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
            Optional<User> optionalUserEmail = userRepository.findUserByEmail(form.getEmail());
            Optional<Seller> optionalUserCnpj = repository.findByCnpj(form.getCnpj());
            if (optionalUserEmail.isPresent()) {
                throw new RequestException("Email already in use.");
            }
            if (optionalUserCnpj.isPresent()) {
                throw new RequestException("Cnpj already in use.");
            }

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
            Optional<Seller> optionalUser = repository.findById(form.getId());
            Optional<User> optionalUserEmail = userRepository.findUserByEmail(form.getEmail());
            Optional<Seller> optionalUserCnpj = repository.findByCnpj(form.getCnpj());
            if (optionalUser.isEmpty()) {
                throw new ResourceNotFoundException(SELLER_NOT_FOUND);
            }
            if (optionalUserEmail.isPresent()) {
                throw new RequestException("Email already in use.");
            }
            if (optionalUserCnpj.isPresent()) {
                throw new RequestException("Cnpj already in use.");
            }

            Seller seller = optionalUser.get();
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
            Optional<Seller> optionalSeller = repository.findById(id);
            if (optionalSeller.isEmpty()) {
                throw new ResourceNotFoundException(SELLER_NOT_FOUND);
            }
            return optionalSeller.get().toResponse();
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
