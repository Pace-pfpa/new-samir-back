package br.gov.agu.samir.new_samir_back.service.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface CalculoCorrecaoMonetaria {

    BigDecimal calcularIndexadorCorrecaoMonetaria(int mes, int ano);
}
