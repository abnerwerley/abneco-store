package com.abneco.delivery.purchase.controller;

import com.abneco.delivery.purchase.json.PurchaseForm;
import com.abneco.delivery.purchase.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService service;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerPurchase(@RequestBody PurchaseForm form) {
        service.registerPurchase(form);
    }
}
