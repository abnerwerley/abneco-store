package com.abneco.delivery.product.controller;

import com.abneco.delivery.address.entity.Address;
import com.abneco.delivery.address.json.AddressForm;
import com.abneco.delivery.address.repository.AddressRepository;
import com.abneco.delivery.address.service.AddressService;
import com.abneco.delivery.product.entity.Product;
import com.abneco.delivery.product.json.ProductForm;
import com.abneco.delivery.product.json.UpdateProductForm;
import com.abneco.delivery.product.repository.ProductRepository;
import com.abneco.delivery.product.service.ProductService;
import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.json.seller.SellerForm;
import com.abneco.delivery.user.repository.SellerRepository;
import com.abneco.delivery.user.service.SellerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    public static final String NAME = "Playstation 5";
    public static final String DESCRIPTION = "The PlayStation 5 (PS5) is a home video game console developed by Sony Interactive Entertainment.";
    public static final BigDecimal PRICE = new BigDecimal("5.5");
    public static final BigDecimal PRICE_ZERO = new BigDecimal("0.0");

    public static final String EMAIL = "email.string@email.com";
    public static final String CNPJ = "12348765324123";
    public static final String SELLER_NAME = "seller1";
    public static final String PASSWORD = "12345678";
    public static final Long PHONE_NUMBER = 11987654321L;

    public static final String CEP = "04555000";
    public static final String COMPLEMENTO = "";
    public static final Integer NUMERO = 123;

    public static final String NEW_NAME = "New product name";
    public static final String NEW_DESCRIPTION = "New description, that has to be longer than 10 char.";
    public static final BigDecimal NEW_PRICE = new BigDecimal("100.0");

    @Test
    void test_get_all_products() throws Exception {
        SellerForm sellerForm = new SellerForm(SELLER_NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        sellerService.register(sellerForm);
        Seller seller = sellerRepository.findByEmail(sellerForm.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        AddressForm addressForm = new AddressForm(seller.getId(), CEP, COMPLEMENTO, NUMERO);
        addressService.registerAddressByCep(addressForm);
        Address address = addressRepository.findByUserId(seller.getId())
                .orElseThrow(() -> new NoSuchElementException("Address not found"));

        ProductForm form = new ProductForm(NAME, DESCRIPTION, PRICE, seller.getId());
        service.registerProduct(form);

        mockMvc.perform(get("/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Product product = repository.findBySellerId(form.getSellerId()).get(0);
        repository.deleteById(product.getId());
        addressRepository.deleteById(address.getId());
        sellerRepository.deleteById(seller.getId());
    }

    @Test
    void test_get_all_products_no_content() throws Exception {
        mockMvc.perform(get("/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void test_get_products_by_seller() throws Exception {
        SellerForm sellerForm = new SellerForm(SELLER_NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        sellerService.register(sellerForm);
        Seller seller = sellerRepository.findByEmail(sellerForm.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        AddressForm addressForm = new AddressForm(seller.getId(), CEP, COMPLEMENTO, NUMERO);
        addressService.registerAddressByCep(addressForm);
        Address address = addressRepository.findByUserId(seller.getId())
                .orElseThrow(() -> new NoSuchElementException("Address not found"));

        ProductForm form = new ProductForm(NAME, DESCRIPTION, PRICE, seller.getId());
        service.registerProduct(form);

        mockMvc.perform(get("/product/seller/" + form.getSellerId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Product product = repository.findBySellerId(form.getSellerId()).get(0);
        repository.deleteById(product.getId());
        addressRepository.deleteById(address.getId());
        sellerRepository.deleteById(seller.getId());
    }

    @Test
    void test_get_products_by_seller_no_content() throws Exception {

        SellerForm sellerForm = new SellerForm(SELLER_NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        sellerService.register(sellerForm);
        Seller seller = sellerRepository.findByEmail(sellerForm.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        AddressForm addressForm = new AddressForm(seller.getId(), CEP, COMPLEMENTO, NUMERO);
        addressService.registerAddressByCep(addressForm);
        Address address = addressRepository.findByUserId(seller.getId())
                .orElseThrow(() -> new NoSuchElementException("Address not found"));

        mockMvc.perform(get("/product/seller/" + seller.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        addressRepository.deleteById(address.getId());
        sellerRepository.deleteById(seller.getId());
    }

    @Test
    void test_get_products_by_seller_not_found() throws Exception {
        mockMvc.perform(get("/product/seller/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Seller not found."));
    }

    @Test
    void test_get_product_by_id_success() throws Exception {
        SellerForm sellerForm = new SellerForm(SELLER_NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        sellerService.register(sellerForm);
        Seller seller = sellerRepository.findByEmail(sellerForm.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        AddressForm addressForm = new AddressForm(seller.getId(), CEP, COMPLEMENTO, NUMERO);
        addressService.registerAddressByCep(addressForm);
        Address address = addressRepository.findByUserId(seller.getId())
                .orElseThrow(() -> new NoSuchElementException("Address not found"));

        ProductForm form = new ProductForm(NAME, DESCRIPTION, PRICE, seller.getId());
        service.registerProduct(form);
        Product product = repository.findBySellerId(form.getSellerId()).get(0);

        mockMvc.perform(get("/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(form.getName()))
                .andExpect(jsonPath("$.description").value(form.getDescription()))
                .andExpect(jsonPath("$.productId").value(product.getId()))
                .andExpect(jsonPath("$.price").value(form.getPrice()))
                .andExpect(jsonPath("$.sellerId").value(product.getSeller().getId()));

        repository.deleteById(product.getId());
        addressRepository.deleteById(address.getId());
        sellerRepository.deleteById(seller.getId());
    }

    @Test
    void test_get_product_by_id_not_found() throws Exception {
        mockMvc.perform(get("/product/a")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Product not found."));
    }

    @Test
    void test_register_product() throws Exception {
        SellerForm sellerForm = new SellerForm(SELLER_NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        sellerService.register(sellerForm);
        Seller seller = sellerRepository.findByEmail(sellerForm.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        AddressForm addressForm = new AddressForm(seller.getId(), CEP, COMPLEMENTO, NUMERO);
        addressService.registerAddressByCep(addressForm);
        Address address = addressRepository.findByUserId(seller.getId())
                .orElseThrow(() -> new NoSuchElementException("Address not found"));

        ProductForm form = new ProductForm(NAME, DESCRIPTION, PRICE, seller.getId());

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isCreated());

        Product product = repository.findBySellerId(seller.getId()).get(0);

        repository.deleteById(product.getId());
        addressRepository.deleteById(address.getId());
        sellerRepository.deleteById(seller.getId());
    }

    @Test
    void test_register_product_zero_price() throws Exception {
        SellerForm sellerForm = new SellerForm(SELLER_NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        sellerService.register(sellerForm);
        Seller seller = sellerRepository.findByEmail(sellerForm.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        AddressForm addressForm = new AddressForm(seller.getId(), CEP, COMPLEMENTO, NUMERO);
        addressService.registerAddressByCep(addressForm);
        Address address = addressRepository.findByUserId(seller.getId())
                .orElseThrow(() -> new NoSuchElementException("Address not found"));

        ProductForm form = new ProductForm(NAME, DESCRIPTION, PRICE_ZERO, seller.getId());

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Product price must neither be below 0.0, nor null."));

        addressRepository.deleteById(address.getId());
        sellerRepository.deleteById(seller.getId());
    }

    @Test
    void test_update_product_success() throws Exception {
        SellerForm sellerForm = new SellerForm(SELLER_NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        sellerService.register(sellerForm);
        Seller seller = sellerRepository.findByEmail(sellerForm.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        AddressForm addressForm = new AddressForm(seller.getId(), CEP, COMPLEMENTO, NUMERO);
        addressService.registerAddressByCep(addressForm);
        Address address = addressRepository.findByUserId(seller.getId())
                .orElseThrow(() -> new NoSuchElementException("Address not found"));

        ProductForm productForm = new ProductForm(NAME, DESCRIPTION, PRICE, seller.getId());
        service.registerProduct(productForm);
        Product product = repository.findBySellerId(productForm.getSellerId()).get(0);

        UpdateProductForm form = new UpdateProductForm(product.getId(), NEW_NAME, NEW_DESCRIPTION, NEW_PRICE);

        mockMvc.perform(put("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk());

        repository.deleteById(product.getId());
        addressRepository.deleteById(address.getId());
        sellerRepository.deleteById(seller.getId());
    }

    @Test
    void test_update_product_not_found() throws Exception {
        UpdateProductForm form = new UpdateProductForm("", NEW_NAME, NEW_DESCRIPTION, NEW_PRICE);

        mockMvc.perform(put("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Product not found."));
    }

    @Test
    void test_delete_product() throws Exception {
        SellerForm sellerForm = new SellerForm(SELLER_NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        sellerService.register(sellerForm);
        Seller seller = sellerRepository.findByEmail(sellerForm.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        AddressForm addressForm = new AddressForm(seller.getId(), CEP, COMPLEMENTO, NUMERO);
        addressService.registerAddressByCep(addressForm);
        Address address = addressRepository.findByUserId(seller.getId())
                .orElseThrow(() -> new NoSuchElementException("Address not found"));

        ProductForm form = new ProductForm(NAME, DESCRIPTION, PRICE, seller.getId());
        service.registerProduct(form);
        Product product = repository.findBySellerId(seller.getId()).get(0);

        mockMvc.perform(delete("/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        addressRepository.deleteById(address.getId());
        sellerRepository.deleteById(seller.getId());
    }

    @Test
    void test_delete_product_not_found() throws Exception {
        SellerForm sellerForm = new SellerForm(SELLER_NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        sellerService.register(sellerForm);
        Seller seller = sellerRepository.findByEmail(sellerForm.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        AddressForm addressForm = new AddressForm(seller.getId(), CEP, COMPLEMENTO, NUMERO);
        addressService.registerAddressByCep(addressForm);
        Address address = addressRepository.findByUserId(seller.getId())
                .orElseThrow(() -> new NoSuchElementException("Address not found"));

        mockMvc.perform(delete("/product/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Product not found."));

        addressRepository.deleteById(address.getId());
        sellerRepository.deleteById(seller.getId());
    }

}
