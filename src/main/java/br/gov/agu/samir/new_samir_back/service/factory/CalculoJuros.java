package br.gov.agu.samir.new_samir_back.service.factory;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public interface CalculoJuros {

    BigDecimal calcularJuros(LocalDate dataAlvo);
}