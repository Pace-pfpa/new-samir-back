package br.gov.agu.samir.new_samir_back.modules.calculadora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoCorrecaoMonetaria {

    TIPO2("INPC"),
    TIPO4("INPC + SELIC"),
    TIPO6("IPCAE + SELIC");

    private String descricao;


    public static TipoCorrecaoMonetaria getTipoCorrecaoMonetaria(String descricao) {
        for (TipoCorrecaoMonetaria tipo : TipoCorrecaoMonetaria.values()) {
            if (tipo.getDescricao().equals(descricao)) {
                return tipo;
            }
        }
        return null;
    }
}
