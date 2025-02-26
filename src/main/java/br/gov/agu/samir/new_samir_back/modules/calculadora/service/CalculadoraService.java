package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.*;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo.CalculoResponseDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo.PlanilhaDeCalculoDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo.ProcessoDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.mapper.ProcessoMapper;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.novo.PlanilhaCalculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CalculadoraService {

    private final HonorariosAdvocaticiosService honorariosAdvocaticiosService;
    private final ResumoProcessoService resumoProcessoService;
    private final CalculoAlcadaService calculoAlcadaService;
    private final RendimentosAcumuladosIRService rendimentosAcumuladosIRService;
    private final TabelaCalculoService tabelaCalculoService;
    private final PlanilhaCalculoService planilhaCalculoService;


    private final ProcessoMapper processoMapper;


    public CalculadoraResponseDTO calcularProcesso(CalculadoraRequestDTO infoCalculo) {

        if (infoCalculo.isAlcada()){
            return calculoAlcadaService.calcularComAlcada(infoCalculo);
        }

        CompletableFuture<BigDecimal> honorarios = calcularHonorarios(infoCalculo);


        int acordo = infoCalculo.getAcordo();

        CalculadoraResponseDTO responseDTO = new CalculadoraResponseDTO();

        List<LinhaTabelaDTO> tabelaCalculo = tabelaCalculoService.gerarTabelaCalculo(infoCalculo);

        BigDecimal valorHonorarios = honorarios.join();

        ResumoProcessoDTO resumoProcesso = resumoProcessoService.gerarResumoProcesso(tabelaCalculo, acordo, valorHonorarios);

        RendimentosAcumuladosIRDTO rendimentosAcumuladosIR = rendimentosAcumuladosIRService.getRendimentosAcumuladosIR(tabelaCalculo, acordo);

        responseDTO.setTabela(tabelaCalculo);

        responseDTO.setResumoProcesso(resumoProcesso);

        responseDTO.setRendimentosAcumuladosIR(rendimentosAcumuladosIR);

        return responseDTO;

    }

    public CalculoResponseDTO calculoProcesso(CalculoRequestDTO requestDTO){

        ProcessoDTO processo = processoMapper.gerarProcessoDTO(requestDTO);

        List<PlanilhaDeCalculoDTO> planilhasCalculo = planilhaCalculoService.getPlanilhasCalculo(requestDTO);






    }

    private CompletableFuture<BigDecimal> calcularHonorarios(CalculadoraRequestDTO requestDTO) {
        if (requestDTO.getHonorariosAdvocaticiosAte() != null) {
            return honorariosAdvocaticiosService.calcularHonorarios(requestDTO);
        }else{
            return CompletableFuture.completedFuture(BigDecimal.ZERO);
        }
    }

}
