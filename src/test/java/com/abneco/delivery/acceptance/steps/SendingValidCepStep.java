package com.abneco.delivery.acceptance.steps;


import com.abneco.delivery.address.service.AddressService;
import com.abneco.delivery.fee.controller.FeeController;
import com.abneco.delivery.fee.dto.CepForm;
import com.abneco.delivery.fee.dto.FeeResponse;
import com.abneco.delivery.fee.service.FeeService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SendingValidCepStep {

    private FeeController controller;
    private CepForm form;

    public static final String CEP = "04851280";

    @Before
    public void setup() {
        RestTemplate restTemplate = new RestTemplate();
        AddressService addressService = new AddressService(restTemplate);
        FeeService service = new FeeService(addressService);
        this.controller = new FeeController(service);
        this.form = new CepForm(CEP);
    }

    @Given("valid cep")
    public void valid_cep() {
        new CepForm(CEP);
    }

    @When("request is made")
    public void request_is_made() {
        controller.getDeliveryFeeByCep(form);
    }

    @Then("FeeResponse is returned according to cep")
    public void feeResponse_is_returned_according_to_cep() {
        FeeResponse response = controller.getDeliveryFeeByCep(form);
        assertNotNull(response);
        assertEquals(new BigDecimal("7.85"), response.getFrete());
        assertEquals("SP", response.getEstado());
    }
}
