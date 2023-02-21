package com.abneco.delivery.acceptance.steps;


import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.fee.controller.FeeController;
import com.abneco.delivery.fee.dto.CepForm;
import com.abneco.delivery.fee.service.FeeService;
import com.abneco.delivery.address.service.AddressService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

public class SendNullCepStep {

    public static final String CEP = null;

    @Given("null cep")
    public void null_cep() {
        CepForm form = new CepForm(CEP);
    }

    @When("request is made with null cep")
    public void request_is_made_with_null_cep() {
        CepForm form = new CepForm(CEP);
        RestTemplate restTemplate = new RestTemplate();
        AddressService addressService = new AddressService(restTemplate);
        FeeService service = new FeeService(addressService);
        FeeController controller = new FeeController(service);
        Exception exception = Assertions.assertThrows(RequestException.class, () -> controller.getDeliveryFeeByCep(form));
        assertNotNull(exception);
    }

    @Then("RequestException is thrown explaining that field cep is mandatory")
    public void requestException_is_thrown_explaining_that_field_cep_is_mandatory() {
        CepForm form = new CepForm(CEP);
        RestTemplate restTemplate = new RestTemplate();
        AddressService addressService = new AddressService(restTemplate);
        FeeService service = new FeeService(addressService);
        FeeController controller = new FeeController(service);
        Exception exception = assertThrows(RequestException.class, () -> controller.getDeliveryFeeByCep(form));
        assertNotNull(exception);
        assertEquals("Cep is mandatory.", exception.getMessage());
    }
}
