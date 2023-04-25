package com.abneco.store.user.service;

import com.abneco.store.exception.RequestException;
import com.abneco.store.exception.ResourceNotFoundException;
import com.abneco.store.user.json.buyer.BuyerForm;
import com.abneco.store.user.json.buyer.BuyerUpdateForm;
import com.abneco.store.user.json.seller.SellerForm;
import com.abneco.store.user.repository.BuyerRepository;
import com.abneco.store.user.repository.SellerRepository;
import com.abneco.store.user.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@NoArgsConstructor
@Slf4j
public abstract class UserService {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected BuyerRepository buyerRepository;

    @Autowired
    protected SellerRepository sellerRepository;

    public static final String ERROR_MESSAGE = "Could not register user.";

    protected static String passwordEncryptor(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public void register(BuyerForm form) {
        try {
            isEmailInUse(form.getEmail());
            isCpfInUse(form.getCpf());
            validate(form);

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (RequestException e) {
            log.error(e.getMessage());
            throw new RequestException(e.getMessage());

        } catch (Exception e) {
            log.error(ERROR_MESSAGE + e.getMessage());
            throw new RequestException(ERROR_MESSAGE);
        }

    }

    protected void isEmailInUse(String email) {
        userRepository.findUserByEmail(email)
                .ifPresent(user -> {
                    throw new RequestException("Email already in use.");
                });
    }

    protected void isCpfInUse(String cnpj) {
        buyerRepository.findByCpf(cnpj)
                .ifPresent(user -> {
                    throw new RequestException("Cpf already in use.");
                });
    }

    protected abstract void validate(BuyerForm form);

    protected abstract void validate(BuyerUpdateForm form);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void register(SellerForm form) {
        try {
            isEmailInUse(form.getEmail());
            isCnpjInUse(form.getCnpj());
            validate(form);

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (RequestException e) {
            log.error(e.getMessage());
            throw new RequestException(e.getMessage());

        } catch (Exception e) {
            log.error(ERROR_MESSAGE + e.getMessage());
            throw new RequestException(ERROR_MESSAGE);
        }
    }

    protected void isCnpjInUse(String cnpj) {
        sellerRepository.findByCnpj(cnpj)
                .ifPresent(sellerCnpj -> {
                    throw new RequestException("Cnpj already in use.");
                });
    }

    protected abstract void validate(SellerForm form);

}
