package com.abneco.delivery.security;

import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImplements implements UserDetailsService {

    @Autowired
    private SellerRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Seller> optionalSeller = repository.findByEmail(username);

        if (optionalSeller.isPresent()) {
            return new UserDetailsImplements(optionalSeller.get());
        } else {
            throw new UsernameNotFoundException("No such user " + username);
        }

    }

}