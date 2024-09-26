package br.gov.agu.samir.new_samir_back.service.factory;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public interface CalculoCorrecaoMonetaria {

    BigDecimal calcularIndexadorCorrecaoMonetaria(String dataAlvo);
}
