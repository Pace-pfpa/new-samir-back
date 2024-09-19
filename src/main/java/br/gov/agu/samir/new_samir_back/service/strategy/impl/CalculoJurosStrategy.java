package br.gov.agu.samir.new_samir_back.service.strategy.impl;

import br.gov.agu.samir.new_samir_back.dtos.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.repository.JurosRepository;
import br.gov.agu.samir.new_samir_back.service.factory.CalculoJurosFactory;
import br.gov.agu.samir.new_samir_back.service.strategy.CalculoJuroStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class CalculoJurosStrategy implements CalculoJuroStrategy {

    private final DateTimeFormatter ddMMyyyy;

    private final CalculoJurosFactory calculoJurosFactory;

    @Override
    public BigDecimal calcularJuros(CalculoRequestDTO requestDTO, String data) {

        if (data.split("/")[1].equals("13")){
            data = data.replace("13", "12");
        }

        if (isDataAntesCitacao(requestDTO, data)){
            String dataCitacao = requestDTO.getDataIncioJuros().withDayOfMonth(1).format(ddMMyyyy);

            return calculoJurosFactory.getCalculo(requestDTO.getTipoJuros()).calcularJuros(dataCitacao);
        }else{
            return calculoJurosFactory.getCalculo(requestDTO.getTipoJuros()).calcularJuros(data);
        }
    }

    private boolean isDataAntesCitacao(CalculoRequestDTO requestDTO, String data) {
        LocalDate dataFormatada = LocalDate.parse(data, ddMMyyyy);
        return dataFormatada.isBefore(requestDTO.getDataIncioJuros());
    }
}
