package br.gov.agu.samir.new_samir_back.modules.beneficio.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;


@Getter
@RequiredArgsConstructor
public enum BeneficiosEnum {
    PENSAO_POR_MORTE_DO_TRABALHADOR_RURAL("1","1 - PENSÃO POR MORTE  DO TRABALHADOR RURAL", Set.of("1","21","87","88","93","SEGURO-DEFESO")),
    APOSENTADORIA_POR_IDADE_DO_TRABALHADOR_RURAL("7","7 - APOSENTADORIA POR IDADE  DO TRABALHADOR RURAL",Set.of("7","36","41","42","46","57","87","88","91","92","94","95","SEGURO-DEFESO")),
    PENSAO_POR_MORTE("21","21 - PENSÃO POR MORTE", Set.of("1","21","87","88","93")),
    AUXILIO_RECLUSAO("25","25 - AUXÍLIO-RECLUSÃO", Set.of("87","88","SEGURO-DEFESO")),
    AUXILIO_DOENCA("31","31 - AUXÍLIO-DOENÇA", Set.of("7","31","32","36","41","42","46","57","80","87","88","91","92","94","SEGURO-DEFESO")),
    APOSENTADORIA_POR_INVALIDEZ("32","32 - APOSENTADORIA POR INCAPACIDADE PERMANENTE PREVIDENCIÁRIA",Set.of("7","31","32","36","41","42","46","57","80","87","88","91","92","94","95","SEGURO-DEFESO")),
    AUXILIO_ACIDENTE("36","36 - AUXÍLIO-ACIDENTE", Set.of("7","32","36","41","42","46","57","87","88","91","92","94","95")),
    APOSENTADORIA_POR_IDADE("41","41 - APOSENTADORIA POR IDADE",Set.of("7","31","32","36","41","42","46","57","87","88","91","92","94","95","SEGURO-DEFESO")),
    APOSENTADORIA_POR_TEMPO_DE_CONTRIBUICAO("42","42 - APOSENTADORIA POR TEMPO DE CONTRIBUIÇÃO",Set.of("7","31","32","36","41","42","46","57","87","88","91","92","94","95","SEGURO-DEFESO")),
    APOSENTADORIA_ESPECIAL("46","46 - APOSENTADORIA ESPECIAL",Set.of("7","31","32","36","41","42","46","57","87","88","91","92","94","95","SEGURO-DEFESO")),
    APOSENTADORIA_POR_TEMPO_DE_CONTRIBUICAO_DO_PROFESSOR("57","57 - APOSENTADORIA POR  TEMPO DE CONTRIBUIÇÃO DE PROFESSOR",Set.of("7","31","32","36","41","42","46","57","87","88","91","92","94","95","SEGURO-DEFESO")),
    SALARIO_MATERNIDADE("80","80 - SALÁRIO-MATERNIDADE",Set.of("31","32","80","87","88","91","92","SEGURO-DEFESO")),
    PENSAO_MENSAL_VITALICIA_DO_SERINGUEIRO("85","85 - PENSÃO MENSAL VITALÍCIA DO SERINGUEIRO",Set.of("85","86","87","88","SEGURO-DEFESO")),
    PENSAO_MENSAL_VITALICIA_DO_DEPENDENTE_DO_SERINGUEIRO("86","86 - PENSÃO MENSAL VITALÍCIA DO DEPENDENTE DO SERINGUEIRO",Set.of("85","86","87","88","SEGURO-DEFESO")),
    LOAS_DEFICIENTE("87","87 - LOAS DEFICIENTE",Set.of("1","7","21","25","31","32","36","41","42","46","57","80","85","86","87","88","91","92","93","94","95","96","SEGURO-DEFESO")),
    LOAS_IDOSO("88","88 - LOAS IDOSO",Set.of("1","7","21","25","31","32","36","41","42","46","57","80","85","86","87","88","91","92","93","94","95","96","SEGURO-DEFESO")),
    AUXILIO_DOENCA_ACIDENTARIO("91","91 - AUXÍLIO-DOENÇA ACIDENTÁRIO",Set.of("31","32","36","41","42","46","57","80","87","88","91","92","94","SEGURO-DEFESO")),
    APOSENTADORIA_POR_INVALIDEZ_ACIDENTARIA("92","92 - APOSENTADORIA POR INVALIDEZ ACIDENTÁRIA",Set.of("7","31","32","36","41","42","46","57","80","87","88","91","92","94","95","SEGURO-DEFESO")),
    PENSAO_POR_MORTE_ACIDENTARIA("93","93 - PENSÃO POR MORTE ACIDENTÁRIA",Set.of("1","21","87","88","93","SEGURO-DEFESO")),
    AUXILIO_ACIDENTE_POR_ACIDENTE_DO_TRABALHO("94","94 - AUXÍLIO-ACIDENTE POR ACIDENTE DO TRABALHO",Set.of("7","32","36","41","42","46","57","87","88","91","92","94","95","SEGURO-DEFESO")),
    AUXILIO_SUPLEMENTAR_POR_ACIDENTE_DO_TRABALHO("95","95 - AUXÍLIO SUPLEMENTAR POR ACIDENTE DO TRABALHO",Set.of("7","32","36","41","42","46","57","87","88","91","92","94","95","SEGURO-DEFESO")),
    PENSAO_ESPECIAL_AO_PORTADOR_DE_HANSENIASE("96","96 - PENSÃO ESPECIAL AO PORTADOR DE HANSENÍASE",Set.of("87","88","96","SEGURO-DEFESO")),
    SEGURO_DEFESO("SEGURO-DEFESO","SEGURO-DEFESO",Set.of("7","21","25","31","32","41","42","46","57","80","85","86","87","88","91","92","93","95","96","SEGURO-DEFESO")),
    SEGURO_DESEMPREGO("SEGURO-DESEMPREGO","SEGURO-DESEMPREGO",Set.of());

    private final String codigo;

    private final String especie;

    private final Set<String> beneficiosInacumulados;


    public static BeneficiosEnum getByNome(String nome) {
        for (BeneficiosEnum beneficio : BeneficiosEnum.values()) {
            if (beneficio.getEspecie().equals(nome)) {
                return beneficio;
            }
        }
        return null;
    }

    public boolean isBeneficioInacumulado(BeneficiosEnum beneficio) {
        return beneficiosInacumulados.contains(beneficio.getCodigo());
    }
}