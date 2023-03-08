package com.abneco.delivery.address.repository;

import com.abneco.delivery.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    void deleteByCep(String cep);
}
