package com.abneco.store.purchase.repository;

import com.abneco.store.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, String> {

    List<Purchase> findByBuyerId(String buyerId);

}
