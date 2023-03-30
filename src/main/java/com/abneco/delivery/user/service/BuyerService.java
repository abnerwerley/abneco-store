package com.abneco.delivery.user.service;

import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.user.entity.Buyer;
import com.abneco.delivery.user.entity.User;
import com.abneco.delivery.user.json.buyer.BuyerForm;
import com.abneco.delivery.user.json.buyer.BuyerResponse;
import com.abneco.delivery.user.repository.BuyerRepository;
import com.abneco.delivery.user.repository.UserRepository;
import com.abneco.delivery.user.utils.ValidateBuyer;
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
public class BuyerService {

    @Autowired
    private BuyerRepository repository;

    @Autowired
    private UserRepository userRepository;

    private static String passwordEncryptor(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public void registerBuyer(BuyerForm form) {
        try {
            Optional<User> optionalUser = userRepository.findUserByEmail(form.getEmail());
            if (optionalUser.isPresent()) {
                throw new RequestException("Email already in use.");
            }
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

    private void save(BuyerForm form) {
        ValidateBuyer.validateBuyer(form);
        Buyer buyer = form.toEntity();
        buyer.setPassword(passwordEncryptor(form.getPassword()));
        repository.save(buyer);
    }
}
