package br.gov.agu.samir.new_samir_back.modules.calculadora.dto;

import br.gov.agu.samir.new_samir_back.modules.calculadora.service.RendimentosAcumuladosIRService;
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
}
