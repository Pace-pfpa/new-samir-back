package br.gov.agu.samir.new_samir_back.service;




import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class DecimoTerceiroService {

    /**
 * Calcula o valor do décimo terceiro salário com base na RMI (Remuneração Mensal Inicial),
 * na data de início do benefício (dataDib) e na data de cálculo (dataCalculo).
 *
 * @param rmi o valor inicial da remuneração mensal
 * @param dataDib a data de início do benefício
 * @param dataCalculo a data de cálculo no formato "dd/MM/yyyy"
 * @return o valor calculado do décimo terceiro salário
 */
public BigDecimal calcularDecimoTerceiro(BigDecimal rmi, LocalDate dataDib, String dataCalculo) {
    int mesesTrabalhados = 12 - dataDib.getMonthValue();
    return isPrimeiroDecimoTerceiro(dataDib, dataCalculo) ?
        rmi.divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(mesesTrabalhados)) : rmi;
}


    private boolean isPrimeiroDecimoTerceiro(LocalDate dataDib, String dataCalculo){
        return Integer.parseInt(dataCalculo.split("/")[2])  == dataDib.getYear();
    }
}

