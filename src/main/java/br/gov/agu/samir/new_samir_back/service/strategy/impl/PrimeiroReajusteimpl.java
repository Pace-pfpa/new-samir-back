package br.gov.agu.samir.new_samir_back.service.strategy.impl;

import br.gov.agu.samir.new_samir_back.dtos.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.repository.IndiceReajusteRepository;
import br.gov.agu.samir.new_samir_back.service.strategy.IndiceReajusteStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class PrimeiroReajusteimpl implements IndiceReajusteStrategy {

    private final DateTimeFormatter ddMMyyyy;

    private final IndiceReajusteRepository indiceReajusteRepository;

    @Override
    public BigDecimal calcularIndiceReajuste(CalculoRequestDTO request, String dataString) {
        LocalDate data = LocalDate.parse(dataString, ddMMyyyy);
        LocalDate dib = request.getDib();

        if (isPrimeiroReajuste(data, dib)) {
            return indiceReajusteRepository.findByData(dib).get().getValor();
        }
        return null;
    }

    private boolean isPrimeiroReajuste(LocalDate data, LocalDate dib) {
        return data.getMonthValue() == 1 && data.getYear() == dib.plusYears(1).getYear();
    }
}