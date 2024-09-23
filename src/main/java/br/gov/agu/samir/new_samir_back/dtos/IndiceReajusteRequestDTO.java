package br.gov.agu.samir.new_samir_back.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndiceReajusteRequestDTO {

    @JsonFormat(shape=JsonFormat.Shape.STRING ,pattern = "dd/MM/yyyy")
    private LocalDate data;

    private BigDecimal valor;

    private LocalDate dataReajuste;

}
