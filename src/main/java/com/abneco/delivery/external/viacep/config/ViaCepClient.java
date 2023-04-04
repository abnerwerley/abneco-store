package com.abneco.delivery.external.viacep.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ViaCepClient {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}