package br.gov.agu.samir.new_samir_back.modules.indice_reajuste.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
