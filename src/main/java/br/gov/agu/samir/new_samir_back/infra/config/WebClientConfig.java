package br.gov.agu.samir.new_samir_back.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://api.bcb.gov.br/dados/serie/bcdata.sgs.4390/dados")
                .build();
    }
}
