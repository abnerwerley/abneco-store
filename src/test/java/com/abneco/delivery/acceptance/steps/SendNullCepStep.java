package com.abneco.delivery.acceptance.steps;


import com.abneco.delivery.address.service.AddressService;
import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.fee.controller.FeeController;
import com.abneco.delivery.fee.dto.CepForm;
import com.abneco.delivery.fee.service.FeeService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

public class SendNullCepStep {

    private final CepForm form = new CepForm(CEP);
    private final RestTemplate restTemplate = new RestTemplate();
    private final AddressService addressService = new AddressService(restTemplate);
    private final FeeService service = new FeeService(addressService);
    private final FeeController controller = new FeeController(service);

    public static final String CEP = null;

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
