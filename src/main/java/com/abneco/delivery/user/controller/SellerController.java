package com.abneco.delivery.user.controller;

import com.abneco.delivery.user.json.seller.SellerForm;
import com.abneco.delivery.user.json.seller.SellerResponse;
import com.abneco.delivery.user.json.seller.UpdateSellerForm;
import com.abneco.delivery.user.service.SellerService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
@AllArgsConstructor
@NoArgsConstructor
public class SellerController {

    @Autowired
    private SellerService service;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerSeller(@RequestBody SellerForm form) {
        service.register(form);
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    public void updateSeller(@RequestBody UpdateSellerForm form) {
        service.updateSeller(form);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SellerResponse getSellerById(@PathVariable String id) {
        return service.findSellerById(id);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SellerResponse>> getAllSellers() {
        List<SellerResponse> sellers = service.findAllSellers();
        if (sellers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sellers);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSellerById(@PathVariable String id) {
        service.deleteSellerById(id);
    }
}
