package com.abneco.store.product.controller;

import com.abneco.store.product.json.ProductForm;
import com.abneco.store.product.json.ProductResponse;
import com.abneco.store.product.json.UpdateProductForm;
import com.abneco.store.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> response = service.getAllProducts();
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/seller/{sellerId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductResponse>> getProductsBySeller(@PathVariable String sellerId) {
        List<ProductResponse> response = service.getProductsBySeller(sellerId);
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductById(@PathVariable String productId) {
        return service.getProductById(productId);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerProduct(@RequestBody ProductForm form) {
        service.registerProduct(form);
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    public void updateProduct(@RequestBody UpdateProductForm form) {
        service.updateProduct(form);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable String productId) {
        service.deleteProductById(productId);
    }
}
