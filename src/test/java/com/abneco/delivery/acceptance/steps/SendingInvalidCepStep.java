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

public class SendingInvalidCepStep {

    public static final String CEP = "A1234567";

    @Given("invalid cep")
    public void invalid_cep() {
        CepForm form = new CepForm(CEP);
    }

    @When("request is made with invalid cep")
    public void request_is_made_with_invalid_cep() {
        CepForm form = new CepForm(CEP);
        RestTemplate restTemplate = new RestTemplate();
        AddressService addressService = new AddressService(restTemplate);
        FeeService service = new FeeService(addressService);
        FeeController controller = new FeeController(service);
        Exception exception = Assertions.assertThrows(RequestException.class, () -> controller.getDeliveryFeeByCep(form));
        assertNotNull(exception);
    }

    @Then("RequestException is thrown explaining the error")
    public void requestException_is_thrown_explaining_the_error() {
        CepForm form = new CepForm(CEP);
        RestTemplate restTemplate = new RestTemplate();
        AddressService addressService = new AddressService(restTemplate);
        FeeService service = new FeeService(addressService);
        FeeController controller = new FeeController(service);
        Exception exception = assertThrows(RequestException.class, () -> controller.getDeliveryFeeByCep(form));
        assertNotNull(exception);
        assertEquals("Please verify if cep has 8 numbers, and numbers only.", exception.getMessage());
    }
}
