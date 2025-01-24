package br.gov.agu.samir.new_samir_back.modules.calculadora.service;


import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class DecimoTerceiroService {

    //TODO: Refatorar
    public BigDecimal calcularDecimoTerceiro(String dataDecimoTerceiro, LocalDate dataInicio, LocalDate fimCalculo, BigDecimal rmi) {

        if (isDecimoTerceiroParcial(dataDecimoTerceiro, dataInicio, fimCalculo)) {
            int mesesTrabalhados = calcularMesesTrabalhados(dataDecimoTerceiro, dataInicio, fimCalculo);
            return rmi.multiply(BigDecimal.valueOf(mesesTrabalhados)).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
        }
        return rmi;
    }


    private int calcularMesesTrabalhados(String decimoTerceiro, LocalDate dataInicio, LocalDate fimCalculo) {

        int anoDecimoTerceiro = Integer.parseInt(decimoTerceiro.split("/")[2]);
        if (anoDecimoTerceiro == dataInicio.getYear()) {

            if (dataInicio.getDayOfMonth() < 17) {
                return 13 - dataInicio.getMonthValue();
            } else {
                return 12 - dataInicio.getMonthValue();
            }
        } else {

            if (fimCalculo.getDayOfMonth() > 17) {
                return fimCalculo.getMonthValue();
            } else {
                return fimCalculo.getMonthValue() - 1;
            }

        }

    }

    private boolean isDecimoTerceiroParcial(String decimoTerceiro, LocalDate dataInicio, LocalDate dataFim) {
        int anoDecimoTerceiro = Integer.parseInt(decimoTerceiro.split("/")[2]);
        return anoDecimoTerceiro == dataInicio.getYear() ||
                anoDecimoTerceiro == dataFim.getYear();
    }
}

