package com.abneco.delivery.purchase.service;

import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.product.entity.Product;
import com.abneco.delivery.product.repository.ProductRepository;
import com.abneco.delivery.purchase.entity.Purchase;
import com.abneco.delivery.purchase.json.PurchaseForm;
import com.abneco.delivery.purchase.json.PurchasePerProduct;
import com.abneco.delivery.purchase.json.PurchaseResponse;
import com.abneco.delivery.purchase.repository.PurchasePerProductRepository;
import com.abneco.delivery.purchase.repository.PurchaseRepository;
import com.abneco.delivery.purchase.utils.ProductPurchaseConverter;
import com.abneco.delivery.user.entity.Buyer;
import com.abneco.delivery.user.repository.BuyerRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseService {

    @Autowired
    private PurchaseRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private PurchasePerProductRepository purchasePerProductRepository;

    @Autowired
    private ProductPurchaseConverter converter;

    public PurchaseResponse getPurchaseById(String purchaseId) {
        try {
            Purchase purchase = getPurchase(purchaseId);
            return purchase.toResponse(productRepository);
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
            Buyer buyer = getBuyer(buyerId);
            List<Purchase> purchases = getAllPurchasesByBuyerId(buyer.getId());
            return purchases.stream()
                    .map(purchase -> purchase.toResponse(productRepository))
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
                    .map(purchase -> purchase.toResponse(productRepository))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Could not get all purchases. " + e.getMessage());
            throw new RequestException("Could not get all purchases.");
        }
    }

    @Transactional
    public void registerPurchase(PurchaseForm form) {
        try {
            Buyer buyer = getBuyer(form.getBuyerId());
            List<PurchasePerProduct> purchasePerProductList = converter.convertToPurchasePerProductList(form.getProductAndQuantityList());

            BigDecimal finalPrice = calculateFinalPrice(purchasePerProductList);

            for (PurchasePerProduct purchasePerProduct : purchasePerProductList) {
                savePurchasePerProduct(purchasePerProduct);
                Product product = purchasePerProduct.getProduct();
                product.addPurchasePerProduct(purchasePerProduct);
                saveProduct(product);
            }
            Purchase purchase = form.toEntity(buyer, purchasePerProductList, finalPrice);
            savePurchase(purchase);
            buyer.setPurchases(purchase);
            saveBuyer(buyer);

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (RequestException e) {
            log.error(e.getMessage());
            throw new RequestException(e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Could not purchase." + e.getMessage());
            throw new RequestException("Could not purchase.");
        }
    }

    public void deletePurchase(String purchaseId) {
        try {
            Purchase purchase = getPurchase(purchaseId);
            repository.deleteById(purchase.getId());

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (Exception e) {
            log.error("Could not delete purchase. " + e.getMessage());
            throw new RequestException("Could not delete purchase.");
        }
    }

    public BigDecimal calculateFinalPrice(List<PurchasePerProduct> productQuantityList) {
        return productQuantityList.stream()
                .map(this::purchasePerProductPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal purchasePerProductPrice(PurchasePerProduct purchasePerProduct) {
        verifyZeroQuantity(purchasePerProduct);
        Product product = getProduct(purchasePerProduct.getProduct().getId());
        return product.getPrice().multiply(new BigDecimal(purchasePerProduct.getQuantity()));
    }

    private void verifyZeroQuantity(PurchasePerProduct purchasePerProduct) {
        if (purchasePerProduct.getQuantity() <= 0) {
            throw new RequestException("You cannot purchase zero product.");
        }
    }

    private List<Purchase> getAllPurchasesByBuyerId(String buyerId) {
        return repository.findByBuyerId(buyerId);
    }

    private Purchase getPurchase(String purchaseId) {
        return repository.findById(purchaseId).orElseThrow(() -> new ResourceNotFoundException("Purchase not found."));
    }

    private Buyer getBuyer(String buyerId) {
        return buyerRepository.findById(buyerId).orElseThrow(() -> new ResourceNotFoundException("Buyer not found."));
    }

    private Product getProduct(String productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new ResourceNotFoundException("Product not found."));
    }

    private void saveProduct(Product product) {
        productRepository.save(product);
    }

    private void saveBuyer(Buyer buyer) {
        buyerRepository.save(buyer);
    }

    private void savePurchase(Purchase purchase) {
        repository.save(purchase);
    }

    private void savePurchasePerProduct(PurchasePerProduct purchasePerProduct) {
        purchasePerProductRepository.save(purchasePerProduct);
    }
}
