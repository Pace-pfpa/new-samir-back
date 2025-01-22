package br.gov.agu.samir.new_samir_back.modules.calculadora.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DescricaoValorIRDTO {
    private String descricao;
    private String valor;
    private String acordo;
    private int competencias;
}
