package com.abneco.delivery.user.service;

import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.user.entity.Buyer;
import com.abneco.delivery.user.json.buyer.BuyerForm;
import com.abneco.delivery.user.json.buyer.BuyerResponse;
import com.abneco.delivery.user.json.buyer.BuyerUpdateForm;
import com.abneco.delivery.user.repository.BuyerRepository;
import com.abneco.delivery.user.repository.UserRepository;
import com.abneco.delivery.user.utils.ValidateBuyer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BuyerService {

    @Autowired
    private BuyerRepository repository;

    @Autowired
    private UserRepository userRepository;

    public static final String BUYER_NOT_FOUND = "Buyer not found.";

    private static String passwordEncryptor(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public void registerBuyer(BuyerForm form) {
        try {
            userRepository.findUserByEmail(form.getEmail())
                    .ifPresent(user -> {
                        throw new RequestException("Email already in use.");
                    });
            repository.findByCpf(form.getCpf())
                    .ifPresent(user -> {
                        throw new RequestException("Cpf already in use.");
                    });
            save(form);

        } catch (RequestException e) {
            log.error(e.getMessage());
            throw new RequestException(e.getMessage());

        } catch (Exception e) {
            log.error("Could not register buyer. " + e.getMessage());
            throw new RequestException("Could not register buyer.");
        }
    }

    public List<BuyerResponse> findAllBuyers() {
        try {
            List<Buyer> buyers = repository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"));
            return buyers.stream()
                    .map(Buyer::toResponse)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Could not get all buyers." + e.getMessage());
            throw new RequestException("Could not get all buyers.");
        }
    }

    public BuyerResponse findBuyerById(String id) {
        try {
            Buyer buyer = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(BUYER_NOT_FOUND));
            return buyer.toResponse();

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (Exception e) {
            log.error("Could not find buyer by id. " + e.getMessage());
            throw new RequestException("Could not find buyer by id.");
        }
    }

    public void updateBuyer(BuyerUpdateForm form) {
        try {
            Buyer buyer = repository.findById(form.getId()).orElseThrow(() -> new ResourceNotFoundException(BUYER_NOT_FOUND));

            userRepository.findUserByEmail(form.getEmail())
                    .ifPresent(user -> {
                        throw new RequestException("Email already in use.");
                    });
            repository.findByCpf(form.getCpf())
                    .ifPresent(user -> {
                        throw new RequestException("Cpf already in use.");
                    });

            buyer.setName(form.getName());
            buyer.setEmail(form.getEmail());
            buyer.setPhoneNumber(form.getPhoneNumber());
            form.setCpf(form.getCpf());
            save(form);

        } catch (ResourceNotFoundException e) {
            log.error("Buyer not found: " + e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (RequestException e) {
            log.error(e.getMessage());
            throw new RequestException(e.getMessage());

        } catch (Exception e) {
            log.error("Could not update Buyer." + e.getMessage());
            throw new RequestException("Could not update buyer.");
        }

    }

    public void deleteBuyerById(String id) {
        try {
            Buyer buyer = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(BUYER_NOT_FOUND));
            repository.delete(buyer);

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RequestException("Could not delete buyer with id: " + id);
        }
    }

    private void save(BuyerForm form) {
        ValidateBuyer.validateBuyer(form);
        Buyer buyer = form.toEntity();
        buyer.setPassword(passwordEncryptor(form.getPassword()));
        repository.save(buyer);
    }

    private void save(BuyerUpdateForm form) {
        ValidateBuyer.validateBuyer(form);
        Buyer buyer = form.toEntity();
        repository.save(buyer);
    }
}
