package br.gov.agu.samir.new_samir_back.service.strategy;

import br.gov.agu.samir.new_samir_back.dtos.CalculoRequestDTO;

import java.math.BigDecimal;

public interface CalculoJuroStrategy {

    BigDecimal calcularJuros(CalculoRequestDTO requestDTO, String data);
}
