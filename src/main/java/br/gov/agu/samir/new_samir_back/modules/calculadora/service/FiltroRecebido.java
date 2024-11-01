package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FiltroRecebido {

    private String data;

    private BigDecimal indiceReajusteRecebido;

    private BigDecimal recebido;


}
