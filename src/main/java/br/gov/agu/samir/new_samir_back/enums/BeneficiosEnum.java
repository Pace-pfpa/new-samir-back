package br.gov.agu.samir.new_samir_back.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BeneficiosEnum {
    PENSAO_POR_MORTE_DO_TRABALHADOR_RURAL("1 - PENSÃO POR MORTE  DO TRABALHADOR RURAL", 1),
    APOSENTADORIA_POR_IDADE_DO_TRABALHADOR_RURAL("7 - APOSENTADORIA POR IDADE  DO TRABALHADOR RURAL", 7),
    PENSAO_POR_MORTE("21 - PENSÃO POR MORTE",21),
    AUXILIO_RECLUSAO("25 - AUXÍLIO-RECLUSÃO", 25),
    AUXILIO_DOENCA("31 - AUXÍLIO-DOENÇA", 31),
    APOSENTADORIA_POR_INVALIDEZ("32 - APOSENTADORIA POR INVALIDEZ", 32),
    AUXILIO_ACIDENTE("36 - AUXÍLIO-ACIDENTE", 36),
    APOSENTADORIA_POR_IDADE("41 - APOSENTADORIA POR IDADE", 41),
    APOSENTADORIA_POR_TEMPO_DE_CONTRIBUICAO("42 - APOSENTADORIA POR TEMPO DE CONTRIBUIÇÃO", 42),
    APOSENTADORIA_ESPECIAL("46 - APOSENTADORIA ESPECIAL", 46),
    APOSENTADORIA_POR_TEMPO_DE_CONTRIBUICAO_DO_PROFESSOR("57 - APOSENTADORIA POR  TEMPO DE CONTRIBUIÇÃO DE PROFESSOR", 57),
    SALARIO_MATERNIDADE("80 - SALÁRIO-MATERNIDADE", 80),
    PENSAO_MENSAL_VITALICIA_DO_SERINGUEIRO("85 - PENSÃO MENSAL VITALÍCIA DO SERINGUEIRO", 85),
    PENSAO_MENSAL_VITALICIA_DO_DEPENDENTE_DO_SERINGUEIRO("86 - PENSÃO MENSAL VITALÍCIA DO DEPENDENTE DO SERINGUEIRO", 86),
    LOAS_DEFICIENTE("87 - LOAS DEFICIENTE", 87),
    LOAS_IDOSO("88 - LOAS IDOSO", 88),
    AUXILIO_DOENCA_ACIDENTARIO("91 - AUXÍLIO-DOENÇA ACIDENTÁRIO", 91),
    APOSENTADORIA_POR_INVALIDEZ_ACIDENTARIA("92 - APOSENTADORIA POR INVALIDEZ ACIDENTÁRIA", 92),
    PENSAO_POR_MORTE_ACIDENTARIA("93 - PENSÃO POR MORTE ACIDENTÁRIA", 93),
    AUXILIO_ACIDENTE_POR_ACIDENTE_DO_TRABALHO("94 - AUXÍLIO-ACIDENTE POR ACIDENTE DO TRABALHO", 94),
    AUXILIO_SUPLEMENTAR_POR_ACIDENTE_DO_TRABALHO("95 - AUXÍLIO SUPLEMENTAR POR ACIDENTE DO TRABALHO", 95),
    PENSAO_ESPECIAL_AO_PORTADOR_DE_HANSENIASE("96 - PENSÃO ESPECIAL AO PORTADOR DE HANSENÍASE", 96),
    SEGURO_DEFESO("XX - SEGURO-DEFESO", 0),
    SEGURO_DESEMPREGO("SEGURO-DESEMPREGO",99 );

    private final String nome;

    private final int codigo;
}