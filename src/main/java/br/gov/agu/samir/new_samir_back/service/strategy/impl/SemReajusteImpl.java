package br.gov.agu.samir.new_samir_back.service.strategy.impl;

import br.gov.agu.samir.new_samir_back.dtos.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.service.strategy.IndiceReajusteStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class SemReajusteImpl implements IndiceReajusteStrategy {

    private final DateTimeFormatter ddMMyyyy;

    @Override
    public BigDecimal calcularIndiceReajuste(CalculoRequestDTO request, String dataString) {

        if (isSemReajuste(dataString)) {
            return BigDecimal.ONE;
        }
        return BigDecimal.ZERO;
    }

    private boolean isSemReajuste(String data) {
        LocalDate dataFormatada = LocalDate.parse(data, ddMMyyyy);
        return dataFormatada.getMonthValue() != 1;
    }
}
