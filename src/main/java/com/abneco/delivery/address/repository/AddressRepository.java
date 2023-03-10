package com.abneco.delivery.address.repository;

import com.abneco.delivery.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    Optional<Address> findBySellerId(String userId);

    @Modifying
    @Query("delete from ADDRESS where address_id=:addressId")
    void deleteById(@Param("addressId") String addressId);
}
