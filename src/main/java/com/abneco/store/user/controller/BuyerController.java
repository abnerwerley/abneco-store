package com.abneco.store.user.controller;

import com.abneco.store.user.json.buyer.BuyerForm;
import com.abneco.store.user.json.buyer.BuyerResponse;
import com.abneco.store.user.json.buyer.BuyerUpdateForm;
import com.abneco.store.user.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buyer")
public class BuyerController {

    @Autowired
    private BuyerService service;

    @GetMapping("")
    public ResponseEntity<List<BuyerResponse>> getAllBuyers() {
        List<BuyerResponse> response = service.findAllBuyers();
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{buyerId}")
    @ResponseStatus(HttpStatus.OK)
    public BuyerResponse findBuyerById(@PathVariable String buyerId) {
        return service.findBuyerById(buyerId);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerBuyer(@RequestBody BuyerForm form) {
        service.register(form);
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    public void updateBuyer(@RequestBody BuyerUpdateForm form) {
        service.updateBuyer(form);
    }

    @DeleteMapping("/{buyerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBuyerById(@PathVariable String buyerId) {
        service.deleteBuyerById(buyerId);
    }
}
