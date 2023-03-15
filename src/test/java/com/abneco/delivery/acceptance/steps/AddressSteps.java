package com.abneco.delivery.acceptance.steps;

import com.abneco.delivery.address.controller.AddressController;
import com.abneco.delivery.address.json.AddressForm;
import com.abneco.delivery.address.mock.AddressMockRepository;
import com.abneco.delivery.address.repository.AddressRepository;
import com.abneco.delivery.address.service.AddressService;
import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.user.mock.MockSellerRepositoryData;
import com.abneco.delivery.user.repository.SellerRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AddressSteps {

    private AddressController controller;
    private AddressForm form;
    private AddressForm invalidForm;
    private AddressForm nullNumberForm;

    public static final String SELLER_ID = "alkdbmncvpasidupqowieursdasd";
    public static final String CEP = "04555-000";
    public static final String COMPLEMENTO = "";
    private String address_id;


    @Before
    public void setup() {
        RestTemplate restTemplate = new RestTemplate();
        AddressRepository repository = new AddressMockRepository();
        SellerRepository sellerRepository = new MockSellerRepositoryData();
        AddressService service = new AddressService(repository, sellerRepository, restTemplate);
        this.controller = new AddressController(service);
    }

    // Scenario: register address with valid address data
    @Given("the following address data:")
    public void address_form_valid_fields(DataTable dataTable) {

        List<Map<String, String>> addresses = dataTable.asMaps();

        for (Map<String, String> address : addresses) {
            String sellerId = address.get("sellerId");
            String cpf = address.get("cep");
            String complemento = address.get("complemento");
            Integer numero = Integer.valueOf(address.get("numero"));
            this.form = new AddressForm(sellerId, cpf, complemento, numero);
        }
    }

    @When("a request to register address with valid data is made")
    public void request_register_address_success() {
        controller.registerAddress(form);
    }

    @Then("the addres should be successfully registered")
    public void address_are_registered() {
        assertDoesNotThrow(() -> controller.registerAddress(form));
    }


    // Scenario: register address with invalid address data

    @Given("the following invalid address data:")
    public void address_form_invalid_fields(DataTable dataTable) {
        List<Map<String, String>> addresses = dataTable.asMaps();

        for (Map<String, String> address : addresses) {
            String sellerId = address.get("selllerId");
            String cpf = address.get("cep");
            String complemento = address.get("complemento");
            Integer numero = Integer.valueOf(address.get("numero"));
            this.invalidForm = new AddressForm(sellerId, cpf, complemento, numero);
        }
    }

    @When("a request to register address with invalid data is made")
    public void request_register_address_fail() {
        assertThrows(RequestException.class, () -> controller.registerAddress(invalidForm));
    }

    @Then("the addres shouldn't be successfully registered")
    public void address_arent_registered() {
        Exception exception = assertThrows(RequestException.class, () -> controller.registerAddress(invalidForm));
        assertEquals("Please verify if cep has 8 numbers, and numbers only.", exception.getMessage());
    }


    // Scenario: register address with null number in addressForm

    @Given("the address form with null number")
    public void address_form_with_null_number() {
        this.nullNumberForm = new AddressForm(SELLER_ID, CEP, COMPLEMENTO, null);
    }

    @When("a request to register address with null number is made")
    public void request_register_address_fail_null_number() {
        assertThrows(RequestException.class, () -> controller.registerAddress(nullNumberForm));
    }

    @Then("the addres shouldn't be registered because number cannot be null")
    public void address_isnt_registered() {
        Exception exception = assertThrows(RequestException.class, () -> controller.registerAddress(nullNumberForm));
        assertEquals("Address number must not be null.", exception.getMessage());
    }


    //Scenario: getting all addresses

    @When("request is made to get all addresses")
    public void request_to_get_all_addresses() {
        controller.getAllAddresses();
    }

    @Then("all addresses should be returned")
    public void all_addresses_are_returned() {
        assertDoesNotThrow(() -> controller.getAllAddresses());
    }

    //Scenario: deleting user address

    @Given("an address id")
    public void an_address_id() {
        this.address_id = "adkshdiodiyuqiwuhlkcbv,xm";
    }

    @When("request is made to delete address by id given")
    public void request_to_delete_address_by_id() {
        controller.deleteAddressById(address_id);
    }

    @Then("address is deleted")
    public void address_is_deleted() {
        assertDoesNotThrow(() -> controller.deleteAddressById(address_id));
    }
}
