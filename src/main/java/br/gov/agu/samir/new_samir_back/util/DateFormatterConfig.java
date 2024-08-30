package br.gov.agu.samir.new_samir_back.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.time.format.DateTimeFormatter;

@Configuration
public class DateFormatterConfig {

    @Bean
    public DateTimeFormatter ddMMyyFormatter(){
        return DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }
}
