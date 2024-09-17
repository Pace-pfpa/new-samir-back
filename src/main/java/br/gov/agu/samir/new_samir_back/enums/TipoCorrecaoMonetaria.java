package br.gov.agu.samir.new_samir_back.enums;

public enum TipoCorrecaoMonetaria {

    TIPO2("INPC"),
    TIPO4("INPC + SELIC"),
    TIPO6("IPCAE + SELIC");

    private String descricao;

    TipoCorrecaoMonetaria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoCorrecaoMonetaria getTipoCorrecaoMonetaria(String descricao) {
        for (TipoCorrecaoMonetaria tipo : TipoCorrecaoMonetaria.values()) {
            if (tipo.getDescricao().equals(descricao)) {
                return tipo;
            }
        }
        return null;
    }
}
