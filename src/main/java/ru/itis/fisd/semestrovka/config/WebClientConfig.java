package ru.itis.fisd.semestrovka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean("emailWebClient")
    public WebClient emailWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8081/email")
                .build();
    }
}
