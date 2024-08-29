package br.gov.agu.samir.new_samir_back.dtos;

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
public class SalarioMinimoResponseDTO {

    private Long id;

    private LocalDate data;

    private BigDecimal valor;
}
