package com.abneco.delivery.user.service;

import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.user.entity.Buyer;
import com.abneco.delivery.user.json.buyer.BuyerForm;
import com.abneco.delivery.user.json.buyer.BuyerResponse;
import com.abneco.delivery.user.json.buyer.BuyerUpdateForm;
import com.abneco.delivery.user.json.seller.SellerForm;
import com.abneco.delivery.user.repository.BuyerRepository;
import com.abneco.delivery.user.repository.UserRepository;
import com.abneco.delivery.user.utils.parameters.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class BuyerService extends UserService {

    @Autowired
    private BuyerRepository repository;

    public BuyerService(BuyerRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public static final String BUYER_NOT_FOUND = "Buyer not found.";

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
            Buyer buyer = getBuyer(id);
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
            userRepository.findUserByEmail(form.getEmail())
                    .ifPresent(user -> {
                        throw new RequestException("Email already in use.");
                    });
            repository.findByCpf(form.getCpf())
                    .ifPresent(user -> {
                        throw new RequestException("Cpf already in use.");
                    });

            Buyer buyer = getBuyer(form.getId());
            buyer.setName(form.getName());
            buyer.setEmail(form.getEmail());
            buyer.setPhoneNumber(form.getPhoneNumber());
            form.setCpf(form.getCpf());
            validate(form);

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
            repository.deleteById(getBuyer(id).getId());

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RequestException("Could not delete buyer with id: " + id);
        }
    }

    private Buyer getBuyer(String buyerId) {
        return repository.findById(buyerId).orElseThrow(() -> new ResourceNotFoundException(BUYER_NOT_FOUND));
    }

    private void save(Buyer buyer) {
        repository.save(buyer);
    }

    @Override
    protected void validate(BuyerForm form) {
        Validate validator = new ValidateCpf(new ValidatePassword(new ValidateUserName(new NothingToValidate())));
        validator.validate(form);
        save(form.toEntity());
    }

    @Override
    protected void validate(BuyerUpdateForm form) {
        Validate validator = new ValidateCpf(new ValidateUserName(new NothingToValidate()));
        validator.validate(form);
        save(form.toEntity());
    }

    @Override
    protected void validate(SellerForm form) {
        //
    }
}
