package br.gov.agu.samir.new_samir_back.service.strategy.impl;

import br.gov.agu.samir.new_samir_back.dtos.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.service.strategy.DecimoTerceiroStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DecimoTerceiroImpl implements DecimoTerceiroStrategy {

    @Override
    public BigDecimal calcularDecimoTerceiro(CalculoRequestDTO requestDTO, BigDecimal salarioReajustado, String dataString) {

        if (isPrimeiroDecimoTerceiro(requestDTO, dataString)) {
            if (isDibEmDezembro(requestDTO)) {
                return BigDecimal.ZERO;
            } else {
                int mesesTrabalhos = 12 - requestDTO.getDib().getMonthValue();
                return salarioReajustado.divide(BigDecimal.valueOf(12), 2).multiply(BigDecimal.valueOf(mesesTrabalhos));
            }
        } else {
            return salarioReajustado;
        }

    }

    private boolean isPrimeiroDecimoTerceiro(CalculoRequestDTO requestDTO, String data){
        return Integer.parseInt(data.split("/")[2])  == requestDTO.getDib().getYear();
    }

    private boolean isDibEmDezembro(CalculoRequestDTO requestDTO) {
        return requestDTO.getDib().getMonthValue() == 12;
    }
}
