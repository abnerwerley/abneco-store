package com.abneco.delivery.purchase.repository;

import com.abneco.delivery.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, String> {

    List<Purchase> findByBuyerId(String buyerId);

}
