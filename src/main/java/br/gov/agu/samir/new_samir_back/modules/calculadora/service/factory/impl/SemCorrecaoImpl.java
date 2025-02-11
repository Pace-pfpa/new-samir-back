package br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.impl;


import br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.interfaces.CalculoCorrecaoMonetaria;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class SemCorrecaoImpl implements CalculoCorrecaoMonetaria {


    @Override
    public BigDecimal calcularIndexadorCorrecaoMonetaria(LocalDate dataCalculo, LocalDate atualizarAte) {
        return BigDecimal.ONE;
    }
}
