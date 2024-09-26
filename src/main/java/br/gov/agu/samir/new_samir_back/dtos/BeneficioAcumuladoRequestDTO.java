package br.gov.agu.samir.new_samir_back.dtos;

import br.gov.agu.samir.new_samir_back.enums.BeneficiosEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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
}
