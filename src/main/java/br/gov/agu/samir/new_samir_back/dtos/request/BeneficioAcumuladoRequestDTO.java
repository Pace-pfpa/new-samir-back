package br.gov.agu.samir.new_samir_back.dtos.request;

import br.gov.agu.samir.new_samir_back.enums.BeneficiosEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeneficioAcumuladoRequestDTO {

    private BeneficiosEnum beneficioAcumulado;

    private LocalDate inicioDesconto;

    private LocalDate fimDesconto;

    private BigDecimal rmi;

    private Integer porcentagemRmi;

    private LocalDate dibAnterior;

    public BigDecimal getRmi(){
        if (porcentagemRmi < 0) {
            throw new IllegalArgumentException("Porcentagem RMI deve ser maior que zero");
        }
        return porcentagemRmi != null ? rmi.multiply(BigDecimal.valueOf(porcentagemRmi).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)) : rmi;
    }
}
