package com.abneco.delivery.purchase.service;

import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.product.entity.Product;
import com.abneco.delivery.product.repository.ProductRepository;
import com.abneco.delivery.purchase.entity.Purchase;
import com.abneco.delivery.purchase.json.PurchaseForm;
import com.abneco.delivery.purchase.repository.PurchaseRepository;
import com.abneco.delivery.user.entity.Buyer;
import com.abneco.delivery.user.repository.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    public void registerPurchase(PurchaseForm form) {
        List<Product> productList = validateProducts(form.getProducts());

        Buyer buyer = buyerRepository.findById(form.getBuyerId()).orElseThrow(() -> new ResourceNotFoundException("Buyer not found."));

        BigDecimal finalPrice = calculateFinalPrice(productList);
        Purchase purchase = form.toEntity(buyer, productList, finalPrice);
        buyer.setPurchases(purchase);

        for (Product product : productList) {
            product.setPurchases(purchase);
        }

        buyerRepository.save(buyer);
        repository.save(purchase);
        productRepository.saveAll(productList);
    }

    private List<Product> validateProducts(List<String> productIds) {
        List<Product> productList = productRepository.findAllById(productIds);

        if (productList.size() != productIds.size()) {
            throw new ResourceNotFoundException("One or more products not found.");
        }

        return productList;
    }

    private BigDecimal calculateFinalPrice(List<Product> productList) {
        return productList
                .stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
