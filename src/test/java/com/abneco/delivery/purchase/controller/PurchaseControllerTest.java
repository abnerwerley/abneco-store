package com.abneco.delivery.purchase.controller;

import com.abneco.delivery.address.entity.Address;
import com.abneco.delivery.address.json.AddressForm;
import com.abneco.delivery.address.repository.AddressRepository;
import com.abneco.delivery.address.service.AddressService;
import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.product.entity.Product;
import com.abneco.delivery.product.json.ProductForm;
import com.abneco.delivery.product.repository.ProductRepository;
import com.abneco.delivery.product.service.ProductService;
import com.abneco.delivery.purchase.entity.Purchase;
import com.abneco.delivery.purchase.json.ProductQuantity;
import com.abneco.delivery.purchase.json.PurchaseForm;
import com.abneco.delivery.purchase.repository.PurchaseRepository;
import com.abneco.delivery.purchase.service.PurchaseService;
import com.abneco.delivery.user.entity.Buyer;
import com.abneco.delivery.user.entity.NaturalPerson;
import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.json.buyer.BuyerForm;
import com.abneco.delivery.user.json.seller.SellerForm;
import com.abneco.delivery.user.repository.BuyerRepository;
import com.abneco.delivery.user.repository.SellerRepository;
import com.abneco.delivery.user.repository.UserRepository;
import com.abneco.delivery.user.service.BuyerService;
import com.abneco.delivery.user.service.SellerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PurchaseControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PurchaseService service;

    @Autowired
    private BuyerService buyerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PurchaseRepository repository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;


    public static final String PRODUCT_NAME = "Playstation 5";
    public static final String PRODUCT_DESCRIPTION = "The PlayStation 5 (PS5) is a home video game console developed by Sony Interactive Entertainment.";
    public static final BigDecimal PRICE = new BigDecimal("5.5");
    public static final String BUYER_ID = "";
    public static final String BUYER_NAME = "Name";
    public static final String SELLER_EMAIL = "seller@email.com";
    public static final String BUYER_EMAIL = "buyer@email.com";
    public static final String PASSWORD = "12345678";
    public static final Long PHONE_NUMBER = 1112345678L;
    public static final String CPF = "12312312312";
    public static final NaturalPerson NATURAL_PERSON = new NaturalPerson(BUYER_EMAIL, CPF, BUYER_NAME, PASSWORD, PHONE_NUMBER, false);
    public static final Buyer BUYER = new Buyer(BUYER_ID, NATURAL_PERSON, "", "");
    public static final int QUANTITY5 = 5;
    public static final SellerForm SELLER_FORM = new SellerForm(BUYER_NAME, SELLER_EMAIL + "1", PASSWORD, PHONE_NUMBER, "12348765324123");
    public static final BuyerForm BUYER_FORM = new BuyerForm(BUYER_NAME, BUYER_EMAIL, PASSWORD, PHONE_NUMBER, CPF);

    @Test
    void test_get_all() throws Exception {
        sellerService.registerSeller(SELLER_FORM);
        Seller seller = sellerRepository.findByEmail(SELLER_FORM.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Seller not found."));

        AddressForm addressForm = new AddressForm(seller.getId(), "04555000", "", 12);
        addressService.registerAddressByCep(addressForm);
        Address address = addressRepository.findByUserId(addressForm.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Address not found."));

        productService.registerProduct(productForm(seller.getId()));
        Product product = productRepository.findBySellerId(seller.getId()).get(0);

        buyerService.registerBuyer(BUYER_FORM);
        Buyer buyer = buyerRepository.findByEmail(BUYER.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Buyer not found."));

        PurchaseForm form = new PurchaseForm(buyer.getId(), List.of(productQuantity(product.getId(), seller)));
        service.registerPurchase(form);

        Purchase purchase = repository.findByBuyerId(buyer.getId()).get(0);

        mockMvc.perform(get("/purchase")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        repository.deleteById(purchase.getId());
        productRepository.deleteById(product.getId());
        addressRepository.deleteById(address.getId());
        userRepository.deleteById(seller.getId());
        buyerRepository.deleteById(buyer.getId());
    }

    @Test
    void test_get_all_no_content() throws Exception {
        mockMvc.perform(get("/purchase")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void test_get_by_purchase_id() throws Exception {
        sellerService.registerSeller(SELLER_FORM);
        Seller seller = sellerRepository.findByEmail(SELLER_FORM.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Seller not found."));

        AddressForm addressForm = new AddressForm(seller.getId(), "04555000", "", 12);
        addressService.registerAddressByCep(addressForm);
        Address address = addressRepository.findByUserId(addressForm.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Address not found."));

        productService.registerProduct(productForm(seller.getId()));
        Product product = productRepository.findBySellerId(seller.getId()).get(0);

        buyerService.registerBuyer(BUYER_FORM);
        Buyer buyer = buyerRepository.findByEmail(BUYER.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Buyer not found."));

        PurchaseForm form = new PurchaseForm(buyer.getId(), List.of(productQuantity(product.getId(), seller)));
        service.registerPurchase(form);

        Purchase purchase = repository.findByBuyerId(buyer.getId()).get(0);

        mockMvc.perform(get("/purchase/" + purchase.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purchaseId").value(purchase.getId()))
                .andExpect(jsonPath("$.buyerId").value(buyer.getId()));

        repository.deleteById(purchase.getId());
        productRepository.deleteById(product.getId());
        addressRepository.deleteById(address.getId());
        userRepository.deleteById(seller.getId());
        buyerRepository.deleteById(buyer.getId());
    }

    @Test
    void test_get_by_purchase_id_not_found() throws Exception {
        mockMvc.perform(get("/purchase/laksdlfkjad")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Purchase not found."));
    }

    @Test
    void test_get_by_buyer_id() throws Exception {
        sellerService.registerSeller(SELLER_FORM);
        Seller seller = sellerRepository.findByEmail(SELLER_FORM.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Seller not found."));

        AddressForm addressForm = new AddressForm(seller.getId(), "04555000", "", 12);
        addressService.registerAddressByCep(addressForm);
        Address address = addressRepository.findByUserId(addressForm.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Address not found."));

        productService.registerProduct(productForm(seller.getId()));
        Product product = productRepository.findBySellerId(seller.getId()).get(0);

        buyerService.registerBuyer(BUYER_FORM);
        Buyer buyer = buyerRepository.findByEmail(BUYER.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Buyer not found."));

        PurchaseForm form = new PurchaseForm(buyer.getId(), List.of(productQuantity(product.getId(), seller)));
        service.registerPurchase(form);

        Purchase purchase = repository.findByBuyerId(buyer.getId()).get(0);

        mockMvc.perform(get("/purchase/buyer/" + buyer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        repository.deleteById(purchase.getId());
        productRepository.deleteById(product.getId());
        addressRepository.deleteById(address.getId());
        userRepository.deleteById(seller.getId());
        buyerRepository.deleteById(buyer.getId());
    }

    @Test
    void test_get_by_buyer_id_not_found() throws Exception {
        mockMvc.perform(get("/purchase/buyer/laksdlfkjad")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Buyer not found."));
    }

    @Test
    void test_get_by_buyer_id_no_content() throws Exception {
        buyerService.registerBuyer(BUYER_FORM);
        Buyer buyer = buyerRepository.findByEmail(BUYER.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Buyer not found."));
        mockMvc.perform(get("/purchase/buyer/" + buyer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        buyerRepository.deleteById(buyer.getId());
    }

    @Test
    void test_register_purchase() throws Exception {

        sellerService.registerSeller(SELLER_FORM);
        Seller seller = sellerRepository.findByEmail(SELLER_FORM.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Seller not found."));

        AddressForm addressForm = new AddressForm(seller.getId(), "04555000", "", 12);
        addressService.registerAddressByCep(addressForm);
        Address address = addressRepository.findByUserId(addressForm.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Address not found."));

        productService.registerProduct(productForm(seller.getId()));
        Product product = productRepository.findBySellerId(seller.getId()).get(0);

        buyerService.registerBuyer(BUYER_FORM);
        Buyer buyer = buyerRepository.findByEmail(BUYER.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Buyer not found."));

        PurchaseForm form = new PurchaseForm(buyer.getId(), List.of(productQuantity(product.getId(), seller)));
        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isCreated());

        Purchase purchase = repository.findByBuyerId(buyer.getId()).get(0);
        repository.delete(purchase);
        productRepository.deleteById(product.getId());
        addressRepository.deleteById(address.getId());
        userRepository.deleteById(seller.getId());
        buyerRepository.deleteById(buyer.getId());
    }

    @Test
    void test_delete_by_purchase_id() throws Exception {
        sellerService.registerSeller(SELLER_FORM);
        Seller seller = sellerRepository.findByEmail(SELLER_FORM.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Seller not found."));

        AddressForm addressForm = new AddressForm(seller.getId(), "04555000", "", 12);
        addressService.registerAddressByCep(addressForm);
        Address address = addressRepository.findByUserId(addressForm.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Address not found."));

        productService.registerProduct(productForm(seller.getId()));
        Product product = productRepository.findBySellerId(seller.getId()).get(0);

        buyerService.registerBuyer(BUYER_FORM);
        Buyer buyer = buyerRepository.findByEmail(BUYER.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Buyer not found."));

        PurchaseForm form = new PurchaseForm(buyer.getId(), List.of(productQuantity(product.getId(), seller)));
        service.registerPurchase(form);

        Purchase purchase = repository.findByBuyerId(buyer.getId()).get(0);

        mockMvc.perform(delete("/purchase/" + purchase.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        productRepository.deleteById(product.getId());
        addressRepository.deleteById(address.getId());
        userRepository.deleteById(seller.getId());
        buyerRepository.deleteById(buyer.getId());
    }

    @Test
    void test_delete_by_purchase_id_not_found() throws Exception {
        mockMvc.perform(delete("/purchase/purchase.getId()")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Purchase not found."));
    }

    public ProductForm productForm(String sellerId) {
        return new ProductForm(PRODUCT_NAME, PRODUCT_DESCRIPTION, PRICE, sellerId);
    }

    public Product product(String productId, Seller seller) {
        return new Product(productId, PRODUCT_NAME, PRODUCT_DESCRIPTION, PRICE, seller);
    }

    public ProductQuantity productQuantity(String productId, Seller seller) {
        return new ProductQuantity(product(productId, seller).getId(), QUANTITY5);
    }
}
