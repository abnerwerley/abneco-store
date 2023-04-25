package com.abneco.store.acceptance.steps;

import com.abneco.store.exception.RequestException;
import com.abneco.store.product.mock.MockProductRepository;
import com.abneco.store.product.repository.ProductRepository;
import com.abneco.store.purchase.controller.PurchaseController;
import com.abneco.store.purchase.json.ProductQuantity;
import com.abneco.store.purchase.json.PurchaseForm;
import com.abneco.store.purchase.mock.MockPurchasePerProductRepository;
import com.abneco.store.purchase.mock.MockPurchaseRepository;
import com.abneco.store.purchase.repository.PurchasePerProductRepository;
import com.abneco.store.purchase.repository.PurchaseRepository;
import com.abneco.store.purchase.service.PurchaseService;
import com.abneco.store.purchase.utils.ProductPurchaseConverter;
import com.abneco.store.user.mock.MockBuyerRepository;
import com.abneco.store.user.repository.BuyerRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseSteps {

    private PurchaseController controller;

    private PurchaseForm form;
    private PurchaseForm zero_quantity_form;

    public static final String BUYER_ID = "";
    public static final String PRODUCT_ID = "";
    public static final int QUANTITY = 3;
    public static final ProductQuantity PRODUCT_QUANTITY = new ProductQuantity(PRODUCT_ID, QUANTITY);
    public static final ProductQuantity PRODUCT_QUANTITY_ZERO = new ProductQuantity(PRODUCT_ID, 0);

    @Before
    public void setup() {
        PurchaseRepository repository = new MockPurchaseRepository();
        ProductRepository productRepository = new MockProductRepository();
        BuyerRepository buyerRepository = new MockBuyerRepository();
        PurchasePerProductRepository purchasePerProductRepository = new MockPurchasePerProductRepository();
        ProductPurchaseConverter converter = new ProductPurchaseConverter(productRepository);
        PurchaseService service = new PurchaseService(repository, productRepository, buyerRepository, purchasePerProductRepository, converter);
        this.controller = new PurchaseController(service);
    }

    @Given("A product and quantity")
    public void purchase_form_valid_data() {
        this.form = new PurchaseForm(BUYER_ID, List.of(PRODUCT_QUANTITY));
    }

    @When("a request to purchase is made")
    public void request_to_purchase() {
        controller.registerPurchase(form);
    }

    @Then("products should be successfully purchased.")
    public void successfull_purchase() {
        assertDoesNotThrow(() -> controller.registerPurchase(form));
    }

    @Given("A product and 0 as quantity")
    public void purchase_form_zero_quantity() {
        this.zero_quantity_form = new PurchaseForm(BUYER_ID, List.of(PRODUCT_QUANTITY_ZERO));
    }

    @When("a request to purchase 0 is made")
    public void request_to_purchase_zero() {
        assertThrows(RequestException.class, () -> controller.registerPurchase(zero_quantity_form));
    }

    @Then("products shouldn't be purchased.")
    public void shouldnt_purchase_zero_quantity() {
        Exception exception = assertThrows(RequestException.class, () -> controller.registerPurchase(zero_quantity_form));
        assertEquals("You cannot purchase zero product.", exception.getMessage());
    }
}
