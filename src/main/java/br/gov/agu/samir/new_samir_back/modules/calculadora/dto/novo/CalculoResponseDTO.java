package br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class CalculoResponseDTO {

    private ProcessoDTO processo;

    private List<PlanilhaDeCalculoDTO> planilhasCalculo;

    private CorrecaoMonetariaDTO correcaoMonetaria;

    private JurosDTO jurosMoratorios;

    private ResumoProcessoDTO resumoProcesso;

}
