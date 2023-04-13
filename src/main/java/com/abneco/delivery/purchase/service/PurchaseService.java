package com.abneco.delivery.purchase.service;

import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.product.entity.Product;
import com.abneco.delivery.product.repository.ProductRepository;
import com.abneco.delivery.purchase.entity.Purchase;
import com.abneco.delivery.purchase.json.ProductQuantity;
import com.abneco.delivery.purchase.json.PurchaseForm;
import com.abneco.delivery.purchase.json.PurchaseResponse;
import com.abneco.delivery.purchase.repository.PurchaseRepository;
import com.abneco.delivery.user.entity.Buyer;
import com.abneco.delivery.user.repository.BuyerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PurchaseService {

    @Autowired
    private PurchaseRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    public PurchaseResponse getPurchaseById(String purchaseId) {
        try {
            Purchase purchase = repository.findById(purchaseId).orElseThrow(() -> new ResourceNotFoundException("Purchase not found."));
            return purchase.toResponse();
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error("Could not get purchase by id. " + e.getMessage());
            throw new RequestException("Could not get purchase by id.");
        }
    }

    public List<PurchaseResponse> getPurchasesByBuyerId(String buyerId) {
        try {
            Buyer buyer = buyerRepository.findById(buyerId).orElseThrow(() -> new ResourceNotFoundException("Buyer not found."));
            List<Purchase> purchases = repository.findByBuyerId(buyer.getId());
            return purchases.stream()
                    .map(Purchase::toResponse)
                    .collect(Collectors.toList());

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (Exception e) {
            log.error("Could not get all purchases. " + e.getMessage());
            throw new RequestException("Could not get purchases by buyer: " + buyerId);
        }
    }

    public List<PurchaseResponse> getAllPurchases() {
        try {
            List<Purchase> purchases = repository.findAll(Sort.by(Sort.Direction.DESC, "purchasedAt"));
            return purchases.stream()
                    .map(Purchase::toResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Could not get all purchases. " + e.getMessage());
            throw new RequestException("Could not get all purchases.");
        }
    }

    @Transactional
    public void registerPurchase(PurchaseForm form) {
        try {
            List<String> productsIds = new ArrayList<>();
            form.getProductAndQuantity().forEach(purchase ->
                    productsIds.add(purchase.getProductId()));
            List<Product> productList = validateProducts(productsIds);
            Buyer buyer = buyerRepository.findById(form.getBuyerId()).orElseThrow(() -> new ResourceNotFoundException("Buyer not found."));
            BigDecimal finalPrice = calculateFinalPrice(form.getProductAndQuantity());
            Purchase purchase = form.toEntity(buyer, productList, finalPrice);
            buyer.setPurchases(purchase);
            for (Product product : productList) {
                product.addPurchase(purchase);
            }
            buyerRepository.save(buyer);
            repository.save(purchase);
            productRepository.saveAll(productList);

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (RequestException e) {
            log.error(e.getMessage());
            throw new RequestException(e.getMessage());

        } catch (Exception e) {
            log.error("Could not purchase." + e.getMessage());
            throw new RequestException("Could not purchase.");
        }
    }

    public void deletePurchase(String purchaseId) {
        try {
            Purchase purchase = repository.findById(purchaseId).orElseThrow(() -> new ResourceNotFoundException("Purchase not found."));
            repository.deleteById(purchase.getId());
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error("Could not delete purchase. " + e.getMessage());
            throw new RequestException("Could not delete purchase.");
        }
    }

    private List<Product> validateProducts(List<String> productIds) {
        List<Product> productList = productRepository.findAllById(productIds);
        if (productList.size() != productIds.size()) {
            throw new RequestException("One or more products not found.");
        }
        return productList;
    }

    public BigDecimal calculateFinalPrice(List<ProductQuantity> productQuantityList) {
        BigDecimal finalPrice = BigDecimal.ZERO;
        for (ProductQuantity productQuantity : productQuantityList) {
            BigDecimal pricePerProduct = purchasePerProduct(productQuantity);
            finalPrice = finalPrice.add(pricePerProduct);
        }
        return finalPrice;
    }

    public BigDecimal purchasePerProduct(ProductQuantity purchasePerProduct) {
        Product product = productRepository.findById(purchasePerProduct.getProductId()).orElseThrow(() ->
                new ResourceNotFoundException("Product not found."));
        return product.getPrice().multiply(new BigDecimal(purchasePerProduct.getQuantity()));
    }
}
