package br.gov.agu.samir.new_samir_back.modules.calculadora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoCorrecaoMonetaria {

    TIPO1(1,"SEM CORREÇÃO MONETÁRIA"),
    TIPO2(2,"MANUAL DE ORIENTAÇÃO DE PROCEDIMENTOS PARA CÁLCULOS NA JF + SELIC prev. - RESOLUÇÃO 267/2013 CJF:URV ATE 06/1994,IPCR ATE 06/1995, INPC ATE 04/1996, IGPDI ATE 08/2006, INPC ATE 11/2021, SELIC ATE 12/2050"),
    TIPO3(3,"MANUAL DE ORIENTAÇÃO DE PROCEDIMENTOS PARA CÁLCULOS NA JF + IPCA-e + SELIC. - RESOLUÇÃO 267/2013 CJF: URV ATE 06/1994, IPCR ATE 06/1995, INPC ATE 04/1996, IGPDI ATE 08/2006, INPC ATE 11/2021, SELIC ATE 12/2050");

    private int tipo;
    private String descricao;


    public static  TipoCorrecaoMonetaria getTipoCorrecaoMonetaria(int tipo){
        for (TipoCorrecaoMonetaria tipoCorrecaoMonetaria : TipoCorrecaoMonetaria.values()) {
            if (tipoCorrecaoMonetaria.getTipo() == tipo){
                return tipoCorrecaoMonetaria;
            }
        }
        throw new IllegalArgumentException("Tipo de correção monetária inválido");
    }
}
