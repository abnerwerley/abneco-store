package com.abneco.delivery.address.controller;

import com.abneco.delivery.address.entity.Address;
import com.abneco.delivery.address.json.AddressForm;
import com.abneco.delivery.address.json.AddressUpdateForm;
import com.abneco.delivery.address.repository.AddressRepository;
import com.abneco.delivery.address.service.AddressService;
import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.json.SellerForm;
import com.abneco.delivery.user.repository.SellerRepository;
import com.abneco.delivery.user.service.SellerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressService service;
    @Autowired
    private SellerService sellerService;

    @Autowired
    private AddressRepository repository;

    @Autowired
    private SellerRepository sellerRepository;
    public static final String EMAIL = "email@email.com";
    public static final String CNPJ = "12348765324123";
    public static final String NAME = "seller1";
    public static final String PASSWORD = "12345678";
    public static final Long PHONE_NUMBER = 11987654321L;
    public static final String CEP = "04555000";
    public static final String COMPLEMENTO = "";
    public static final Integer NUMERO = 123;

    @Test
    void test_get_all_addresses() throws Exception {
        SellerForm sellerForm = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        sellerService.registerSeller(sellerForm);
        Seller seller = sellerRepository.findByEmail(sellerForm.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        AddressForm addressForm = new AddressForm(seller.getId(), CEP, COMPLEMENTO, NUMERO);
        service.registerAddressByCep(addressForm);
        Address address = repository.findByUserId(seller.getId())
                .orElseThrow(() -> new NoSuchElementException("Address not found"));

        mockMvc.perform(get("/address")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        repository.deleteById(address.getId());
        sellerRepository.deleteById(seller.getId());
    }

    @Test
    void test_get_all_addresses_no_content() throws Exception {
        mockMvc.perform(get("/address")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void test_delete_address_by_id() throws Exception {
        SellerForm sellerForm = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        sellerService.registerSeller(sellerForm);
        Seller seller = sellerRepository.findByEmail(sellerForm.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        AddressForm addressForm = new AddressForm(seller.getId(), CEP, COMPLEMENTO, NUMERO);
        service.registerAddressByCep(addressForm);
        Address address = repository.findByUserId(seller.getId())
                .orElseThrow(() -> new NoSuchElementException("Address not found"));

        mockMvc.perform(delete("/address/" + address.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        repository.deleteById(address.getId());
        sellerRepository.deleteById(seller.getId());
    }

    @Test
    void test_delete_address_by_id_not_found() throws Exception {
        mockMvc.perform(delete("/address/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_register_address_success() throws Exception {
        SellerForm sellerForm = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        sellerService.registerSeller(sellerForm);
        Seller seller = sellerRepository.findByEmail(sellerForm.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        AddressForm addressForm = new AddressForm(seller.getId(), CEP, COMPLEMENTO, NUMERO);

        mockMvc.perform(post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressForm)))
                .andExpect(status().isCreated());

        Address address = repository.findByUserId(seller.getId())
                .orElseThrow(() -> new NoSuchElementException("Address not found"));
        repository.deleteById(address.getId());
        sellerRepository.deleteById(seller.getId());
    }

    @Test
    void test_register_address_unexistent_user() throws Exception {
        AddressForm addressForm = new AddressForm("ajçdkjfaçkdn,cmvnlkjdfaçljkdfpoiuret", CEP, COMPLEMENTO, NUMERO);

        mockMvc.perform(post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressForm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("User does not exist."));
    }

    @Test
    void test_register_address_user_already_has_address() throws Exception {
        SellerForm sellerForm = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        sellerService.registerSeller(sellerForm);
        Seller seller = sellerRepository.findByEmail(sellerForm.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        AddressForm addressForm = new AddressForm(seller.getId(), CEP, COMPLEMENTO, NUMERO);
        service.registerAddressByCep(addressForm);

        mockMvc.perform(post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressForm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("User must only has one address."));

        Address address = repository.findByUserId(seller.getId())
                .orElseThrow(() -> new NoSuchElementException("Address not found"));
        repository.deleteById(address.getId());
        sellerRepository.deleteById(seller.getId());
    }

    @Test
    void test_update_address_success() throws Exception {
        SellerForm sellerForm = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        sellerService.registerSeller(sellerForm);
        Seller seller = sellerRepository.findByEmail(sellerForm.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        AddressForm addressForm = new AddressForm(seller.getId(), CEP, COMPLEMENTO, NUMERO);
        service.registerAddressByCep(addressForm);
        Address address = repository.findByUserId(seller.getId())
                .orElseThrow(() -> new NoSuchElementException("Address not found"));

        AddressUpdateForm form = new AddressUpdateForm(address.getId(), seller.getId(), "79116070", "Lado par", 321);

        mockMvc.perform(put("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk());

        repository.deleteById(address.getId());
        sellerRepository.deleteById(seller.getId());
    }

    @Test
    void test_update_address_address_not_found() throws Exception {
        SellerForm sellerForm = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        sellerService.registerSeller(sellerForm);
        Seller seller = sellerRepository.findByEmail(sellerForm.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        AddressUpdateForm form = new AddressUpdateForm("ajçdkjfaçkdn,cmvnlkjdfaçljkdfpoiuret", seller.getId(), "79116070", "Lado par", 321);

        mockMvc.perform(put("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Address not found."));

        sellerRepository.deleteById(seller.getId());
    }

    @Test
    void test_update_address_user_not_found() throws Exception {
        SellerForm sellerForm = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        sellerService.registerSeller(sellerForm);
        Seller seller = sellerRepository.findByEmail(sellerForm.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        AddressForm addressForm = new AddressForm(seller.getId(), CEP, COMPLEMENTO, NUMERO);
        service.registerAddressByCep(addressForm);
        Address address = repository.findByUserId(seller.getId())
                .orElseThrow(() -> new NoSuchElementException("Address not found"));

        AddressUpdateForm form = new AddressUpdateForm(address.getId(), "ajçdkjfaçkdn,cmvnlkjdfaçljkdfpoiuret", "79116070", "Lado par", 321);

        mockMvc.perform(put("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Seller not found."));

        repository.deleteById(address.getId());
        sellerRepository.deleteById(seller.getId());
    }
}
