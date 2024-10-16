package br.gov.agu.samir.new_samir_back.service;




import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class DecimoTerceiroService {


public BigDecimal calcularDecimoTerceiro(String dataDecimoTerceiro,LocalDate dataDib, BigDecimal rmi) {

    int mesesTrabalhados = 12 - dataDib.getMonthValue();

    if (dataDib.getDayOfMonth() < 17) {
        mesesTrabalhados = 13 - dataDib.getMonthValue();
    }

    BigDecimal valorDecimoTerceiro = rmi.divide(BigDecimal.valueOf(12),2, RoundingMode.HALF_UP);
    valorDecimoTerceiro = valorDecimoTerceiro.multiply(BigDecimal.valueOf(mesesTrabalhados));

    return isPrimeiroDecimoTerceiro(dataDecimoTerceiro, dataDib) ? valorDecimoTerceiro : rmi;
}


    private boolean isPrimeiroDecimoTerceiro(String decimoTerceiro, LocalDate dataDib){
        return Integer.parseInt(decimoTerceiro.split("/")[2])  == dataDib.getYear();
    }
}

