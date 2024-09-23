package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.util.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CalculoRmiService {

    private final DateUtils dateUtils;

    /**
     * Calcula a RMI (Remuneração Mensal Inicial) com base no valor de RMI fornecido e na string de data.
     *
     * @param rmi o valor inicial da remuneração mensal
     * @param dataString a string de data no formato "dd/MM/yyyy"
     * @return o valor calculado da RMI para a data fornecida
     */
    public BigDecimal calcularRmi(BigDecimal rmi, String dataString){

        LocalDate data = dateUtils.mapStringToLocalDate(dataString);

        int diasTrabalhados = 31 - data.getDayOfMonth();

        return rmi.divide(BigDecimal.valueOf(30), RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(diasTrabalhados));

    }
}
