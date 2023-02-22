package com.abneco.delivery.user.repository;

import com.abneco.delivery.user.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, String> {

    @Query("select s from SELLER as s where email=:email")
    Optional<Seller> findByEmail(@Param("email") String email);
}
