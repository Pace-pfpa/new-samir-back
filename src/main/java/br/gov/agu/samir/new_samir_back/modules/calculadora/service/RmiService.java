package br.gov.agu.samir.new_samir_back.modules.calculadora.service;


import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.CalculadoraRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.beneficio.enums.BeneficiosEnum;

import br.gov.agu.samir.new_samir_back.modules.indice_reajuste.model.IndiceReajusteModel;
import br.gov.agu.samir.new_samir_back.modules.indice_reajuste.repository.IndiceReajusteRepository;
import br.gov.agu.samir.new_samir_back.modules.salario_minimo.service.SalarioMinimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RmiService {

    private final SalarioMinimoService salarioMinimoService;
    private final IndiceReajusteRepository indiceReajusteRepository;
    private final CalculoIndiceReajusteService calculoIndiceReajusteService;


    private static final EnumSet<BeneficiosEnum> BENEFICIOS_QUE_RECEBEM_MENOS_QUE_SALARIO_MINIMO = EnumSet.of(
            BeneficiosEnum.AUXILIO_ACIDENTE,
            BeneficiosEnum.AUXILIO_ACIDENTE_POR_ACIDENTE_DO_TRABALHO
    );

    //TODO REFATORAR
    public BigDecimal reajustarRmi(CalculadoraRequestDTO infoCalculo){

       if (isReajusteNecessario(infoCalculo)){
           LocalDate dataCalculo = infoCalculo.getDib().plusYears(1L).withDayOfMonth(1);
           List<BigDecimal> indicesParaReajuste = new ArrayList<>();
           indicesParaReajuste.add(retornaPrimeiroReajuste(infoCalculo.getDib(), infoCalculo.getDibAnterior()));
           int anoCalculo = dataCalculo.getYear();
           int anoFim = infoCalculo.getDataInicio().getYear();

           while (anoCalculo < anoFim){
               IndiceReajusteModel indiceReajuste = indiceReajusteRepository.findByData(dataCalculo.withDayOfMonth(1)).orElseThrow();
               BigDecimal indice = indiceReajusteRepository.findFirstByDataReajuste(indiceReajuste.getDataReajuste().getData()).getValor();
               indicesParaReajuste.add(indice);
               dataCalculo = dataCalculo.plusYears(1);
               anoCalculo++;
           }

           return indicesParaReajuste.stream().reduce(infoCalculo.getRmi(),BigDecimal::multiply);
       }

       return infoCalculo.getRmi();
    }


    public BigDecimal calcularValorDevido(LocalDate dataCalculo, LocalDate inicioCalculo, LocalDate fimCalculo, BigDecimal rmiConservada){

        if (dataCalculo.isEqual(fimCalculo)){
            int diasTrabalhados = fimCalculo.getDayOfMonth();
            return rmiConservada.divide(BigDecimal.valueOf(30),2, RoundingMode.UP).multiply(BigDecimal.valueOf(diasTrabalhados));
        }

        if (isInicioEFimNoMesmoMesEAno(inicioCalculo,fimCalculo)){
            int diasTrabalhados = 1 + fimCalculo.getDayOfMonth() - inicioCalculo.getDayOfMonth();
            BigDecimal valor = rmiConservada.divide(BigDecimal.valueOf(30),2, RoundingMode.UP).multiply(BigDecimal.valueOf(diasTrabalhados));
            return valor;
        }

        if (dataCalculo.getDayOfMonth() == 1){
            return rmiConservada;
        }
        int diasTrabalhados = 31 - dataCalculo.getDayOfMonth();

        return rmiConservada.divide(BigDecimal.valueOf(30),2, RoundingMode.UP).multiply(BigDecimal.valueOf(diasTrabalhados));
    }


    public BigDecimal obterSalarioMinimoOuRmi(BigDecimal rmi, LocalDate dib, BeneficiosEnum beneficio){
        if (BENEFICIOS_QUE_RECEBEM_MENOS_QUE_SALARIO_MINIMO.contains(beneficio)){
            BigDecimal metadeSalarioMinimoEpoca = salarioMinimoService.getSalarioMinimoProximoPorDataNoMesmoAno(dib).multiply(BigDecimal.valueOf(0.5));
            return isRmiInferiorSalarioMinimo(rmi,metadeSalarioMinimoEpoca) ? metadeSalarioMinimoEpoca : rmi;
        }

        BigDecimal salarioMinimoNaEpoca = salarioMinimoService.getSalarioMinimoProximoPorDataNoMesmoAno(dib);
        return isRmiInferiorSalarioMinimo(rmi,salarioMinimoNaEpoca) ? salarioMinimoNaEpoca : rmi;
    }


    private boolean isInicioEFimNoMesmoMesEAno(LocalDate inicioCalculo, LocalDate fimCalculo){
        return inicioCalculo.getMonth().equals(fimCalculo.getMonth()) && inicioCalculo.getYear() == fimCalculo.getYear();
    }

    private boolean isReajusteNecessario(CalculadoraRequestDTO infoCalculo){
        LocalDate dataInicio = infoCalculo.getDataInicio();
        LocalDate dib = infoCalculo.getDib();
        return dataInicio.isAfter(dib) && dataInicio.getYear() != dib.getYear();
    }

    private boolean isRmiInferiorSalarioMinimo(BigDecimal valorRmi, BigDecimal valorSalarioMinimoNaEpoca){
        return valorRmi.compareTo(valorSalarioMinimoNaEpoca) < 0;
    }

    private BigDecimal retornaPrimeiroReajuste(LocalDate dib, LocalDate dibAnterior){
        if (dibAnterior == null){
            return calculoIndiceReajusteService.calcularPrimeiroReajusteSemDibAnterior(dib);
        }
        return calculoIndiceReajusteService.calcularPrimeiroReajusteComDibAnterior(dib, dibAnterior);
    }

}
