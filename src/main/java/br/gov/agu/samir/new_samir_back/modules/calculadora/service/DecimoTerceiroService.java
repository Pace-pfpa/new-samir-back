package br.gov.agu.samir.new_samir_back.modules.calculadora.service;




import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class DecimoTerceiroService {


public BigDecimal calcularDecimoTerceiro(String dataDecimoTerceiro,LocalDate dataInicio, BigDecimal rmi) {

    int mesesTrabalhados = 12 - dataInicio.getMonthValue();

    if (dataInicio.getDayOfMonth() < 17) {
        mesesTrabalhados = 13 - dataInicio.getMonthValue();
    }

    BigDecimal valorDecimoTerceiro = rmi.divide(BigDecimal.valueOf(12),2, RoundingMode.HALF_UP);
    valorDecimoTerceiro = valorDecimoTerceiro.multiply(BigDecimal.valueOf(mesesTrabalhados));

    return isPrimeiroDecimoTerceiro(dataDecimoTerceiro, dataInicio) ? valorDecimoTerceiro : rmi;
}


    private boolean isPrimeiroDecimoTerceiro(String decimoTerceiro, LocalDate dataInicio){
        return Integer.parseInt(decimoTerceiro.split("/")[2])  == dataInicio.getYear();
    }
}

