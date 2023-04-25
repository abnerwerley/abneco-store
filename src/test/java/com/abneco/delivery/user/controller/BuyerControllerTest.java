package com.abneco.delivery.user.controller;

import com.abneco.delivery.user.entity.Buyer;
import com.abneco.delivery.user.entity.User;
import com.abneco.delivery.user.json.buyer.BuyerForm;
import com.abneco.delivery.user.json.buyer.BuyerUpdateForm;
import com.abneco.delivery.user.repository.BuyerRepository;
import com.abneco.delivery.user.repository.UserRepository;
import com.abneco.delivery.user.service.BuyerService;
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
public class BuyerControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BuyerService service;

    @Autowired
    private BuyerRepository repository;

    @Autowired
    private UserRepository userRepository;


    public static final String NAME = "Name";
    public static final String EMAIL = "string.test@email.com";
    public static final String PASSWORD = "12345678";
    public static final Long PHONE_NUMBER = 1112345678L;
    public static final String CPF = "12312312312";
    public static final String SHORT_CPF = "1231231231";


    public static final String NEW_NAME = "New Name";
    public static final String NEW_EMAIL = "updated.email@email.com";
    public static final Long NEW_PHONE_NUMBER = 1112345678L;
    public static final String NEW_CPF = "21312312312";
    public static final BuyerForm BUYER_FORM = new BuyerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CPF);
    public static final BuyerUpdateForm UPDATE_FORM = new BuyerUpdateForm("1482639841628346", NEW_NAME, NEW_EMAIL, NEW_PHONE_NUMBER, NEW_CPF);

    @Test
    void test_get_all_buyers() throws Exception {
        service.register(BUYER_FORM);
        User user = userRepository.findUserByEmail(BUYER_FORM.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Buyer not found"));

        mockMvc.perform(get("/buyer")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        repository.deleteById(user.getId());
    }

    @Test
    void test_get_all_buyers_no_content() throws Exception {
        mockMvc.perform(get("/buyer")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void test_find_buyer_by_id() throws Exception {
        BUYER_FORM.setCpf(CPF);
        service.register(BUYER_FORM);

        Buyer buyer = repository.findByEmail(BUYER_FORM.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Buyer not found"));

        mockMvc.perform(get("/buyer/" + buyer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(buyer.getId()))
                .andExpect(jsonPath("email").value(buyer.getEmail()))
                .andExpect(jsonPath("cpf").value(buyer.getCpf()));

        repository.deleteById(buyer.getId());
    }

    @Test
    void test_find_buyer_by_id_not_found() throws Exception {
        mockMvc.perform(get("/buyer/alkjdflakjdlakjdf")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Buyer not found."));
    }

    @Test
    void test_register_seller() throws Exception {
        mockMvc.perform(post("/buyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BUYER_FORM)))
                .andExpect(status().isCreated());

        User user = userRepository.findUserByEmail(BUYER_FORM.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Buyer not found"));

        repository.deleteById(user.getId());
    }

    @Test
    void test_register_seller_short_cpf() throws Exception {
        BUYER_FORM.setCpf(SHORT_CPF);
        mockMvc.perform(post("/buyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BUYER_FORM)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Cpf must have 11 numbers, and numbers only."));
    }

    @Test
    void test_delete_buyer_by_id() throws Exception {
        service.register(BUYER_FORM);

        Buyer buyer = repository.findByEmail(BUYER_FORM.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Buyer not found"));

        mockMvc.perform(delete("/buyer/" + buyer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void test_delete_buyer_by_id_not_found() throws Exception {
        mockMvc.perform(delete("/buyer/alkjdflakjdlakjdf")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Buyer not found."));
    }

}
