package com.abneco.delivery.product.repository;

import com.abneco.delivery.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("select p from PRODUCT p where seller_id=:sellerId")
    List<Product> findBySellerId(@Param("sellerId") String sellerId);
}
