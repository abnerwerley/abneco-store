package com.abneco.store.address.repository;

import com.abneco.store.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    Optional<Address> findByUserId(String userId);

    @Modifying
    @Query("delete from ADDRESS where address_id=:addressId")
    void deleteById(@Param("addressId") String addressId);
}
