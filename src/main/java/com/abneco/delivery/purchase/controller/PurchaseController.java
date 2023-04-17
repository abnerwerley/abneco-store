package com.abneco.delivery.purchase.controller;

import com.abneco.delivery.purchase.json.PurchaseForm;
import com.abneco.delivery.purchase.json.PurchaseResponse;
import com.abneco.delivery.purchase.service.PurchaseService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase")
@NoArgsConstructor
public class PurchaseController {

    @Autowired
    private PurchaseService service;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PurchaseResponse>> getAllPurchases() {
        List<PurchaseResponse> response = service.getAllPurchases();
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(service.getAllPurchases());
    }

    @GetMapping("/{purchaseId}")
    @ResponseStatus(HttpStatus.OK)
    public PurchaseResponse getPurchaseById(@PathVariable String purchaseId) {
        return service.getPurchaseById(purchaseId);
    }

    @GetMapping("/buyer/{buyerId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PurchaseResponse>> getPurchasesByBuyerId(@PathVariable String buyerId) {
        List<PurchaseResponse> response = service.getPurchasesByBuyerId(buyerId);
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(service.getPurchasesByBuyerId(buyerId));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerPurchase(@RequestBody PurchaseForm form) {
        service.registerPurchase(form);
    }

    @DeleteMapping("/{purchaseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePurchase(@PathVariable String purchaseId) {
        service.deletePurchase(purchaseId);
    }
}
