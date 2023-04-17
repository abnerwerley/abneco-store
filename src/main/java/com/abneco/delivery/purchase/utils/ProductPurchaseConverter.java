package com.abneco.delivery.purchase.utils;

import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.product.entity.Product;
import com.abneco.delivery.product.repository.ProductRepository;
import com.abneco.delivery.purchase.json.ProductQuantity;
import com.abneco.delivery.purchase.json.PurchasePerProduct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class ProductPurchaseConverter {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductQuantity> convertToProductQuantity(List<PurchasePerProduct> purchasePerProductList) {
        return purchasePerProductList.stream()
                .map(purchasePerProduct -> new ProductQuantity(
                        getProduct(purchasePerProduct.getProduct().getId()).getId(),
                        purchasePerProduct.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<PurchasePerProduct> convertToPurchasePerProductList(List<ProductQuantity> productQuantityList) {
        return productQuantityList.stream()
                .map(productQuantity -> new PurchasePerProduct(
                        getProduct(productQuantity.getProductId()),
                        productQuantity.getQuantity()))
                .collect(Collectors.toList());
    }


    public List<String> getProductIdsFromProductQuantityList(List<ProductQuantity> productQuantityList) {
        return productQuantityList.stream()
                .map(productQuantity -> getProduct(productQuantity.getProductId()).getId())
                .collect(Collectors.toList());
    }

    public List<String> getProductIdsFromPurchasePerProductList(List<PurchasePerProduct> purchasePerProductList) {
        return purchasePerProductList.stream()
                .map(purchasePerProduct -> getProduct(purchasePerProduct.getProduct().getId()).getId())
                .collect(Collectors.toList());
    }

    private Product getProduct(String productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found."));
    }

}
