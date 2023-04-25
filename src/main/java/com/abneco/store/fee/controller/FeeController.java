package com.abneco.store.fee.controller;

import com.abneco.store.fee.dto.CepForm;
import com.abneco.store.fee.service.FeeService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/v1")
public class FeeController {

    @Autowired
    private FeeService service;

    @PostMapping("/consulta-endereco")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal getDeliveryFeeByCep(@RequestBody CepForm form) {
        return service.generateResponse(form.getCep());
    }
}

