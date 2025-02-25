package br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ValoresOriginaisDTO {

    private BigDecimal somaPrincipal;

    private BigDecimal somaJuros;

    private BigDecimal devidoAoReclamante;

    private BigDecimal honorariosAdvocaticios;

    private BigDecimal total;
}
