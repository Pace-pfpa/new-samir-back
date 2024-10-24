package br.gov.agu.samir.new_samir_back.service;


import br.gov.agu.samir.new_samir_back.enums.BeneficiosEnum;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import java.util.EnumSet;



@Service
@AllArgsConstructor
public class RmiService {

    private final SalarioMinimoService salarioMinimoService;

    private static final EnumSet<BeneficiosEnum> BENEFICIOS_QUE_RECEBEM_MENOS_QUE_SALARIO_MINIMO = EnumSet.of(
            BeneficiosEnum.AUXILIO_ACIDENTE,
            BeneficiosEnum.AUXILIO_ACIDENTE_POR_ACIDENTE_DO_TRABALHO
    );



    public BigDecimal calcularValorDevido(LocalDate dataCalculo, LocalDate fimCalculo, BigDecimal rmiConservada){
        if (dataCalculo.isEqual(fimCalculo)){
            int diasTrabalhados = fimCalculo.getDayOfMonth();
            return rmiConservada.divide(BigDecimal.valueOf(30),2, RoundingMode.UP).multiply(BigDecimal.valueOf(diasTrabalhados));
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

    private boolean isRmiInferiorSalarioMinimo(BigDecimal valorRmi, BigDecimal valorSalarioMinimoNaEpoca){
        return valorRmi.compareTo(valorSalarioMinimoNaEpoca) < 0;
    }
}
