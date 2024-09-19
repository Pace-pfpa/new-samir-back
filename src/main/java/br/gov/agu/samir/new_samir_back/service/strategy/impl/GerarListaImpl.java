package br.gov.agu.samir.new_samir_back.service.strategy.impl;

import br.gov.agu.samir.new_samir_back.dtos.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.repository.BeneficioRepository;
import br.gov.agu.samir.new_samir_back.service.strategy.GerarListaStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class GerarListaImpl implements GerarListaStrategy {

    private final BeneficioRepository beneficioRepository;

    @Override
    public List<String> gerarLista(CalculoRequestDTO requestDTO) {

        if (isBeneficioSemDecimoTerceiro(requestDTO)) {
            return gerarListaDeDatasSemDecimoTerceiro(requestDTO.getDib(), requestDTO.getDataFim());
        }else{
            return gerarListaDataComDecimoTerceiro(requestDTO.getDib(), requestDTO.getDataFim());
        }
    }

    private boolean isBeneficioSemDecimoTerceiro(CalculoRequestDTO requestDTO) {
        return  requestDTO.getBeneficio() == beneficioRepository.findByNome("87 - LOAS DEFICIENTE").getNome()
                && requestDTO.getBeneficio() == beneficioRepository.findByNome("88 - LOAS IDOSO").getNome()
                && requestDTO.getBeneficio() == beneficioRepository.findByNome("XX - SEGURO-DEFESO").getNome();

    }

    private static List<String> gerarListaDataComDecimoTerceiro(LocalDate startDate, LocalDate endDate) {
        List<String> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate.format(formatter));
            if (currentDate.getMonthValue() == 12) {
                LocalDate month13 = currentDate.plusMonths(1);
                dates.add("01/13/" + (currentDate.getYear()));
                currentDate = month13.withDayOfMonth(1);
            } else {
                currentDate = currentDate.plusMonths(1).withDayOfMonth(1);
            }
        }
        return dates;
    }

    private static List<String> gerarListaDeDatasSemDecimoTerceiro(LocalDate startDate, LocalDate endDate) {
        List<String> formattedDates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Adiciona a data inicial formatada
        formattedDates.add(startDate.format(formatter));

        // Itera de mês em mês até a data final
        LocalDate tempDate = startDate.withDayOfMonth(1).plusMonths(1);
        while (tempDate.isBefore(endDate) || tempDate.isEqual(endDate)) {
            formattedDates.add(tempDate.format(formatter));
            tempDate = tempDate.plusMonths(1);
        }

        // Adiciona a data final formatada, se necessário
        if (!formattedDates.contains(endDate.format(formatter))) {
            formattedDates.add(endDate.format(formatter));
        }

        return formattedDates;
    }
}
