package br.gov.agu.samir.new_samir_back.modules.calculadora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum TipoJuros {

    TIPO1(1,"JUROS + SELIC" );

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
