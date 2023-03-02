package com.abneco.delivery.acceptance.steps;


import com.abneco.delivery.address.service.AddressService;
import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.fee.controller.FeeController;
import com.abneco.delivery.fee.dto.CepForm;
import com.abneco.delivery.fee.service.FeeService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

public class SendNullCepStep {

    private FeeController controller;
    private CepForm form;

    public static final String CEP = null;

    @Before
    public void setup() {
        RestTemplate restTemplate = new RestTemplate();
        AddressService addressService = new AddressService(restTemplate);
        FeeService service = new FeeService(addressService);
        this.controller = new FeeController(service);
        this.form = new CepForm(CEP);
    }

    @Given("null cep")
    public void null_cep() {
        new CepForm(CEP);
    }

    @When("request is made with null cep")
    public void request_is_made_with_null_cep() {
        assertThrows(RequestException.class, () -> controller.getDeliveryFeeByCep(form));
    }

    @Then("RequestException is thrown explaining that field cep is mandatory")
    public void requestException_is_thrown_explaining_that_field_cep_is_mandatory() {
        Exception exception = assertThrows(RequestException.class, () -> controller.getDeliveryFeeByCep(form));
        assertNotNull(exception);
        assertEquals("Cep is mandatory.", exception.getMessage());
    }
}
