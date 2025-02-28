package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.*;
import br.gov.agu.samir.new_samir_back.modules.calculadora.enums.TipoCorrecaoMonetaria;
import br.gov.agu.samir.new_samir_back.modules.calculadora.enums.TipoJuros;
import br.gov.agu.samir.new_samir_back.modules.calculadora.mapper.ProcessoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculoService {

    private final ProcessoMapper processoMapper;
    private final PlanilhaCalculoService planilhaCalculoService;
    private final ResumoProcessoService resumoProcessoService;

    public CalculoResponseDTO calculoProcesso(CalculoRequestDTO requestDTO) {

        ProcessoDTO processo = processoMapper.gerarProcessoDTO(requestDTO);

        List<PlanilhaDeCalculoDTO> planilhasCalculo = planilhaCalculoService.getPlanilhasCalculo(requestDTO);

        CorrecaoMonetariaDTO correcaoMonetariaDTO = new CorrecaoMonetariaDTO(TipoCorrecaoMonetaria.getTipoCorrecaoMonetaria(requestDTO.getTipoCorrecao()).getDescricao());

        JurosDTO jurosDTO = new JurosDTO(TipoJuros.TIPO1.getDescricao());

        ResumoProcessoDTO resumoProcessoDTO = resumoProcessoService.gerarResumoProcesso(planilhasCalculo,requestDTO);

        return new CalculoResponseDTO(processo,planilhasCalculo,correcaoMonetariaDTO,jurosDTO,resumoProcessoDTO);
    }

}
