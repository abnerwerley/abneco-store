package com.abneco.delivery.acceptance.steps;

import com.abneco.delivery.address.mock.AddressMockRepository;
import com.abneco.delivery.address.repository.AddressRepository;
import com.abneco.delivery.product.controller.ProductController;
import com.abneco.delivery.product.json.ProductForm;
import com.abneco.delivery.product.mock.MockProductRepository;
import com.abneco.delivery.product.repository.ProductRepository;
import com.abneco.delivery.product.service.ProductService;
import com.abneco.delivery.user.mock.MockSellerRepositoryData;
import com.abneco.delivery.user.repository.SellerRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ProductSteps {


    private ProductController controller;
    private ProductForm form;

    public static final String NAME = "Playstation 5";
    public static final String DESCRIPTION = "The PlayStation 5 (PS5) is a home video game console developed by Sony Interactive Entertainment.";
    public static final BigDecimal PRICE = new BigDecimal("5.500");
    public static final BigDecimal PRICE_ZERO = new BigDecimal("0.0");
    public static final String SELLER_ID = "hdkahjdlkfajhdkfjahkdfhaksdjhf";

    @Before
    public void setup() {
        ProductRepository repository = new MockProductRepository();
        SellerRepository sellerRepository = new MockSellerRepositoryData();
        AddressRepository addressRepository = new AddressMockRepository();
        ProductService service = new ProductService(repository, sellerRepository, addressRepository);
        this.controller = new ProductController(service);
    }

    @Given("valid product form data")
    public void valid_product_form_data() {
        this.form = new ProductForm(NAME, DESCRIPTION, PRICE, SELLER_ID);
    }

    @When("a request to register product with valid data is made")
    public void request_to_register_product_valid_data() {
        controller.registerProduct(form);
    }

    @Then("the product should be successfully registered")
    public void product_registered() {
        assertDoesNotThrow(() -> controller.registerProduct(form));
    }
}
