package br.gov.agu.samir.new_samir_back.modules.calculadora.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculadoraResponseDTO {

    private List<LinhaTabelaDTO> tabela;

    private ResumoProcessoDTO resumoProcesso;

    private RendimentosAcumuladosIRDTO rendimentosAcumuladosIR;

    private AnaliseJuizadoEspecialFederalDTO analiseJuizadoEspecialFederal;
}
