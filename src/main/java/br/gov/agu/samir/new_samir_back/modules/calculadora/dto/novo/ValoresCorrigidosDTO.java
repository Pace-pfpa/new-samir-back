package br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ValoresCorrigidosDTO {

    private BigDecimal somaPrincipalCorrigida;

    private BigDecimal somaJurosCorrigidos;

    private BigDecimal devidoAoReclamanteCorrigido;

    private BigDecimal honorariosAdvocaticiosCorrigidos;

    private BigDecimal totalCorrigido;
}
