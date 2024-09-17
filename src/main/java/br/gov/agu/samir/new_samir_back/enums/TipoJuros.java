package br.gov.agu.samir.new_samir_back.enums;

public enum TipoJuros {

    TIPO2("JUROS + SELIC");

    private String descricao;

    TipoJuros(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
