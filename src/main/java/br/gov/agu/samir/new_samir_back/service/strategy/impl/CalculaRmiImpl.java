package br.gov.agu.samir.new_samir_back.service.strategy.impl;

import br.gov.agu.samir.new_samir_back.dtos.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.service.strategy.RmiStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@AllArgsConstructor
public class CalculaRmiImpl implements RmiStrategy {


    @Override
    public BigDecimal calcularRmi(CalculoRequestDTO requestDTO, String dataString) {
        if (isRmiParcial(dataString)) {
            int diasTrabalhados = 31 - Integer.parseInt(dataString.split("/")[0]);
            return requestDTO.getRmi()
                    .divide(BigDecimal.valueOf(30), RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(diasTrabalhados));
        }
        return requestDTO.getRmi();
    }


    private boolean isRmiParcial(String data){
        return !data.split("/")[0].equals("01");
    }
}
