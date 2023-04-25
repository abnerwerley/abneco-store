package com.abneco.store.user.controller;

import com.abneco.store.user.entity.Seller;
import com.abneco.store.user.json.seller.SellerForm;
import com.abneco.store.user.json.seller.UpdateSellerForm;
import com.abneco.store.user.repository.SellerRepository;
import com.abneco.store.user.service.SellerService;
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
class SellerControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SellerService service;

    @Autowired
    private SellerRepository repository;

    public static final String EMAIL = "email.string@email.com";
    public static final String EMAIL2 = "email2.string@email.com";
    public static final String CNPJ = "12348765324123";
    public static final String NEW_CNPJ = "12348765876543";
    public static final String NAME = "seller1";
    public static final String PASSWORD = "12345678";
    public static final Long PHONE_NUMBER = 11987654321L;

    @Test
    void test_register_seller() throws Exception {
        SellerForm form = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        mockMvc.perform(post("/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isCreated());

        Seller seller = repository.findByEmail(form.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        repository.deleteById(seller.getId());
    }

    @Test
    void test_register_seller_email_already_in_use() throws Exception {
        SellerForm formToBeSaved = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        SellerForm form = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        service.register(formToBeSaved);

        mockMvc.perform(post("/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Email already in use."));

        Seller seller = repository.findByEmail(formToBeSaved.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));
        repository.deleteById(seller.getId());
    }

//    @Test
//    void test_register_seller_cnpj_already_in_use() throws Exception {
//        SellerForm formToBeSaved = new SellerForm(NAME, EMAIL2, PASSWORD, PHONE_NUMBER, CNPJ);
//        SellerForm form = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
//        service.register(formToBeSaved);
//
//        mockMvc.perform(post("/seller")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(form)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.detail").value("Cnpj already in use."));
//
//        Seller seller = repository.findByEmail(formToBeSaved.getEmail())
//                .orElseThrow(() -> new NoSuchElementException("Seller not found"));
//        repository.deleteById(seller.getId());
//
//        Seller seller2 = repository.findByEmail(form.getEmail())
//                .orElseThrow(() -> new NoSuchElementException("Seller not found"));
//        if (seller2 != null)
//            repository.deleteById(seller2.getId());
//    }

    @Test
    void test_register_seller_email_not_formated() throws Exception {
        SellerForm form = new SellerForm(NAME, "email", PASSWORD, PHONE_NUMBER, CNPJ);
        mockMvc.perform(post("/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Email has incorrect format."));
    }

    @Test
    void test_get_all_sellers() throws Exception {
        SellerForm form = new SellerForm(NAME, EMAIL2, PASSWORD, PHONE_NUMBER, CNPJ);
        service.register(form);

        mockMvc.perform(get("/seller")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Seller seller = repository.findByEmail(form.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));
        repository.deleteById(seller.getId());
    }

    @Test
    void test_get_all_sellers_no_content() throws Exception {
        mockMvc.perform(get("/seller")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void test_get_seller_by_id_success() throws Exception {
        SellerForm form = new SellerForm(NAME, EMAIL2, PASSWORD, PHONE_NUMBER, CNPJ);
        service.register(form);
        Seller seller = repository.findByEmail(form.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        mockMvc.perform(get("/seller/" + seller.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Seller1"))
                .andExpect(jsonPath("$.email").value(form.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(form.getPhoneNumber()))
//                .andExpect(jsonPath("$.cnpj").value(form.getCnpj()))
        ;

        repository.deleteById(seller.getId());
    }

    @Test
    void test_get_seller_by_id_not_found() throws Exception {
        mockMvc.perform(get("/seller/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Seller not found."));
    }

    @Test
    void test_delete_seller_by_id_success() throws Exception {
        SellerForm form = new SellerForm(NAME, EMAIL2, PASSWORD, PHONE_NUMBER, CNPJ);
        service.register(form);
        Seller seller = repository.findByEmail(form.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        mockMvc.perform(delete("/seller/" + seller.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        repository.deleteById(seller.getId());
    }

    @Test
    void test_delete_seller_by_id_not_found() throws Exception {
        mockMvc.perform(delete("/seller/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Seller not found."));
    }

    @Test
    void test_update_seller_success() throws Exception {
        SellerForm formToBeSaved = new SellerForm(NAME, EMAIL2, PASSWORD, PHONE_NUMBER, CNPJ);
        service.register(formToBeSaved);
        Seller seller = repository.findByEmail(formToBeSaved.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        UpdateSellerForm form = new UpdateSellerForm(seller.getId(), "new name", EMAIL, 1798701234L, NEW_CNPJ);

        mockMvc.perform(put("/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk());

        repository.deleteById(seller.getId());
    }

    @Test
    void test_update_seller_not_found() throws Exception {
        UpdateSellerForm form = new UpdateSellerForm("id", "new name", EMAIL, 1798701234L, NEW_CNPJ);

        mockMvc.perform(put("/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Seller not found."));
    }
}
