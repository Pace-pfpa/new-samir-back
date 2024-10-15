package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.request.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.exceptions.ResourceNotFoundException;
import br.gov.agu.samir.new_samir_back.repository.IndiceReajusteRepository;
import br.gov.agu.samir.new_samir_back.util.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;


@Service
@AllArgsConstructor
public class CalculoIndiceReajusteService {


    private final IndiceReajusteRepository indiceReajusteRepository;


    public BigDecimal calcularIndiceReajusteAnual(LocalDate dataCalculo, LocalDate dib, LocalDate dibAnterior){
        if (dataCalculo.getMonthValue() != 1){
            return BigDecimal.ONE;
        }

        if (isPrimeiroReajuste(dataCalculo, dib)){
            return dibAnterior != null ? calcularPrimeiroReajusteComDibAnterior(dib, dibAnterior) : calcularPrimeiroReajusteSemDibAnterior(dib);
        }
        return indiceReajusteRepository.findFirstByDataReajuste(dataCalculo.withDayOfMonth(1)).getValor();
    }

    private boolean isPrimeiroReajuste(LocalDate dataCalculo, LocalDate dib){
        return dataCalculo.getMonthValue() == 1 && dataCalculo.getYear() == dib.plusYears(1L).getYear();
    }

    private BigDecimal calcularPrimeiroReajusteComDibAnterior(LocalDate dib, LocalDate dibAnterior){
        LocalDate dataReajusteDibAtual = indiceReajusteRepository.findByData(dib.withDayOfMonth(1)).orElseThrow().getDataReajuste();
        LocalDate dataReajusteDibAnterior = indiceReajusteRepository.findByData(dibAnterior.withDayOfMonth(1)).orElseThrow().getDataReajuste();

        if (dataReajusteDibAtual.isEqual(dataReajusteDibAnterior)){
            return indiceReajusteRepository.findByData(dibAnterior.withDayOfMonth(1)).orElseThrow().getValor();
        }
        return indiceReajusteRepository.findFirstByDataReajuste(dataReajusteDibAtual).getValor();
    }

    private BigDecimal calcularPrimeiroReajusteSemDibAnterior(LocalDate dib){
        return indiceReajusteRepository.findByData(dib.withDayOfMonth(1)).orElseThrow().getValor();
    }
}
