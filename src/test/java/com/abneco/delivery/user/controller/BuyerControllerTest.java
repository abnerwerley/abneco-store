package com.abneco.delivery.user.controller;

import com.abneco.delivery.user.entity.User;
import com.abneco.delivery.user.json.buyer.BuyerForm;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    void test_register_seller() throws Exception {
        BuyerForm form = new BuyerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CPF);
        mockMvc.perform(post("/buyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isCreated());

        User user = userRepository.findUserByEmail(form.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Buyer not found"));

        repository.deleteById(user.getId());
    }

    @Test
    void test_register_seller_short_cpf() throws Exception {
        BuyerForm form = new BuyerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, SHORT_CPF);
        mockMvc.perform(post("/buyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Cpf must have 11 numbers, and numbers only."));
    }

    @Test
    void test_get_all_buyers() throws Exception {
        BuyerForm form = new BuyerForm(NAME, EMAIL, PASSWORD, PHONE_NUMBER, CPF);
        service.registerBuyer(form);
        User user = userRepository.findUserByEmail(form.getEmail())
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

}
