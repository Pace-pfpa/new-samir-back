package br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor @NoArgsConstructor
public class CompetenciaDTO {

    private String data;

    private BigDecimal indiceReajusteDevido;

    private BigDecimal devido;

    private BigDecimal indiceReajusteRecebido;

    private BigDecimal recebido;

    private BigDecimal diferenca;

    private BigDecimal indiceCorrecaoMonetaria;

    private BigDecimal salarioCorrigido;

    private BigDecimal porcentagemJuros;

    private BigDecimal juros;

    private BigDecimal soma;


}
