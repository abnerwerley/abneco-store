package com.abneco.delivery.user.service;

import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.json.buyer.BuyerForm;
import com.abneco.delivery.user.json.buyer.BuyerUpdateForm;
import com.abneco.delivery.user.json.seller.SellerForm;
import com.abneco.delivery.user.json.seller.SellerResponse;
import com.abneco.delivery.user.json.seller.UpdateSellerForm;
import com.abneco.delivery.user.repository.SellerRepository;
import com.abneco.delivery.user.repository.UserRepository;
import com.abneco.delivery.user.utils.parameters.*;
import com.abneco.delivery.utils.DateFormatter;
import com.abneco.delivery.utils.UpperCaseFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SellerService extends UserService {

    public SellerService(SellerRepository repository, UserRepository userRepository) {
        this.sellerRepository = repository;
        this.userRepository = userRepository;
    }

    public static final String SELLER_NOT_FOUND = "Seller not found.";

    public void updateSeller(UpdateSellerForm form) {
        try {
            Seller seller = getSeller(form.getId());
            userRepository.findUserByEmail(form.getEmail())
                    .ifPresent(user -> {
                        throw new RequestException("Email already in use.");
                    });

            sellerRepository.findByCnpj(form.getCnpj())
                    .ifPresent(sellerCnpj -> {
                        throw new RequestException("Cnpj already in use.");
                    });

            seller.setName(UpperCaseFormatter.formatToCapitalLetter(form.getName()));
            seller.setEmail(form.getEmail());
            seller.setPhoneNumber(form.getPhoneNumber());
            seller.setCnpj(form.getCnpj());
            seller.setUpdatedAt(DateFormatter.formatNow());
            validateAndSave(seller, form);

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
            Seller seller = getSeller(id);
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
            List<Seller> sellers = sellerRepository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"));
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
            Seller seller = getSeller(id);
            sellerRepository.delete(seller);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RequestException("Could not delete seller with id: " + id);
        }
    }

    private Seller getSeller(String id) {
        return sellerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(SELLER_NOT_FOUND));
    }

    private void validateAndSave(Seller seller, UpdateSellerForm form) {
        Validate validator = new ValidateEmail(new ValidateCnpj(new ValidateUserName(new NothingToValidate())));
        validator.validate(form);
        save(seller);
    }

    private void save(Seller seller) {
        sellerRepository.save(seller);
    }

    @Override
    protected void validate(SellerForm form) {
        Validate validator = new ValidateEmail(new ValidateCnpj(new ValidateUserName(new ValidatePassword(new NothingToValidate()))));
        validator.validate(form);
        Seller seller = form.toEntity();
        seller.setPassword(passwordEncryptor(form.getPassword()));
        save(seller);
    }

    @Override
    protected void validate(BuyerForm form) {
        //
    }

    @Override
    protected void validate(BuyerUpdateForm form) {
        //
    }
}
