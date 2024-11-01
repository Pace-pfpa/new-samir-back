package br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.interfaces;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;


@Component
public interface CalculoCorrecaoMonetaria {

    BigDecimal calcularIndexadorCorrecaoMonetaria(LocalDate dataCalculo, LocalDate atualizarAte);
}
