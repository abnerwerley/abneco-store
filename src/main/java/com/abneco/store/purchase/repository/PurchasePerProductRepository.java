package com.abneco.store.purchase.repository;

import com.abneco.store.purchase.json.PurchasePerProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchasePerProductRepository extends JpaRepository<PurchasePerProduct, String> {

    @Query("select ppp from PURCHASE_PER_PRODUCT as ppp where product_id=:productId")
    Optional<PurchasePerProduct> findByProductId(@Param("productId") String productId);
}
