package com.abneco.delivery.acceptance.steps;


import com.abneco.delivery.address.service.AddressService;
import com.abneco.delivery.fee.controller.FeeController;
import com.abneco.delivery.fee.dto.CepForm;
import com.abneco.delivery.fee.dto.FeeResponse;
import com.abneco.delivery.fee.service.FeeService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SendingValidCepStep {

    private final CepForm form = new CepForm(CEP);
    private final RestTemplate restTemplate = new RestTemplate();
    private final AddressService addressService = new AddressService(restTemplate);
    private final FeeService service = new FeeService(addressService);
    private final FeeController controller = new FeeController(service);
    public static final String CEP = "04851280";

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
