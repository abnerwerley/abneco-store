package com.abneco.delivery.user.controller;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        Seller seller = repository.findByEmail(EMAIL).get();
        repository.deleteById(seller.getId());
    }

    @Test
    void test_register_seller_email_already_in_use() throws Exception {
        SellerForm formToBeSaved = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        SellerForm form = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        service.registerSeller(formToBeSaved);

        mockMvc.perform(post("/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Email already in use."));

        Seller seller = repository.findByEmail(formToBeSaved.getEmail()).get();
        repository.deleteById(seller.getId());
    }

    @Test
    void test_register_seller_cnpj_already_in_use() throws Exception {
        SellerForm formToBeSaved = new SellerForm(NAME, EMAIL2, PASSWORD, PHONE_NUMBER, CNPJ);
        SellerForm form = new SellerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CNPJ);
        service.registerSeller(formToBeSaved);

        mockMvc.perform(post("/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Cnpj already in use."));

        Seller seller = repository.findByCnpj(formToBeSaved.getCnpj()).get();
        repository.deleteById(seller.getId());
    }

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
        service.registerSeller(form);

        mockMvc.perform(get("/seller")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Seller seller = repository.findByEmail(EMAIL2).get();
        repository.deleteById(seller.getId());
    }

    @Test
    void test_get_all_sellers_no_content() throws Exception {
        mockMvc.perform(get("/seller")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
