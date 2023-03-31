package com.abneco.delivery.user.repository;

import com.abneco.delivery.user.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, String> {

    Optional<Buyer> findByEmail(String email);

    @Query("select b from BUYER b where cpf=:cpf")
    Optional<Buyer> findByCpf(@Param("cpf") String cpf);
}
