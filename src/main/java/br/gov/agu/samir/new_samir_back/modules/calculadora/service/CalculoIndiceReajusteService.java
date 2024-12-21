package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.indice_reajuste.repository.IndiceReajusteRepository;
import lombok.AllArgsConstructor;
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

    public BigDecimal calcularPrimeiroReajusteComDibAnterior(LocalDate dib, LocalDate dibAnterior){
        LocalDate dataReajusteDibAtual = indiceReajusteRepository.findByData(dib.withDayOfMonth(1)).orElseThrow().getDataReajuste().getData();
        LocalDate dataReajusteDibAnterior = indiceReajusteRepository.findByData(dibAnterior.withDayOfMonth(1)).orElseThrow().getDataReajuste().getData();

        if (dataReajusteDibAtual.isEqual(dataReajusteDibAnterior)){
            return indiceReajusteRepository.findByData(dibAnterior.withDayOfMonth(1)).orElseThrow().getValor();
        }
        return indiceReajusteRepository.findFirstByDataReajuste(dataReajusteDibAtual).getValor();
    }

    public BigDecimal calcularPrimeiroReajusteSemDibAnterior(LocalDate dib){
        return indiceReajusteRepository.findByData(dib.withDayOfMonth(1)).orElseThrow().getValor();
    }
}
