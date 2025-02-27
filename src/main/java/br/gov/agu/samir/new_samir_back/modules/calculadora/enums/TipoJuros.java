package br.gov.agu.samir.new_samir_back.modules.calculadora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum TipoJuros {

    TIPO1(1,"MANUAL DE ORIENTAÇÃO DE PROCEDIMENTOS PARA CÁLCULOS NA JF - RESOLUÇÃO 784/2022 CJF:\n" +
            "ORTN ATÉ 02/1986, OTN ATÉ 02/1989, BTN ATÉ 03/1990, IPC-IBGE ATÉ 02/1991, INPC ATÉ\n" +
            "12/1992, IRSM ATÉ 02/1994, URV ATÉ 06/1994, IPCR ATÉ 06/1995, INPC ATÉ 04/1996,\n" +
            "IGPDI ATÉ 08/2006, INPC ATÉ 11/2021, SELIC PREV. EC/113 ATÉ 12/2050\n" );

    private  int tipo;

    private String descricao;

    public static TipoJuros getByTipo(int tipo) {
        for (TipoJuros tipoJuros : TipoJuros.values()) {
            if (tipoJuros.getTipo() == tipo) {
                return tipoJuros;
            }
        }
        return null;
    }

}
