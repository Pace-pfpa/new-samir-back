package br.gov.agu.samir.new_samir_back.modules.calculadora.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DescricaoValorDTO {
    private String descricao;
    private BigDecimal totalPeriodoCalculo;
    private BigDecimal calculoParaExecucao;
}
