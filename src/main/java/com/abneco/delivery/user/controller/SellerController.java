package com.abneco.delivery.user.controller;

import com.abneco.delivery.user.json.SellerForm;
import com.abneco.delivery.user.json.SellerResponse;
import com.abneco.delivery.user.json.SellerUpdateForm;
import com.abneco.delivery.user.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private SellerService service;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerSeller(@RequestBody SellerForm form) {
        service.registerSeller(form);
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    public SellerResponse updateSeller(@RequestBody SellerUpdateForm form) {
        return service.updateSeller(form);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SellerResponse getSellerById(@PathVariable String id) {
        return service.findSellerById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSellerById(@PathVariable String id) {
        service.deleteSellerById(id);
    }
}
