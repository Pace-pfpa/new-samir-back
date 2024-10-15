package br.gov.agu.samir.new_samir_back.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalculoResponseDTO {

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