package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.AnaliseJuizadoEspecialFederalDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.CalculadoraRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.CalculadoraResponseDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.LinhaTabelaDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.RendimentosAcumuladosIRDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.ResumoProcessoDTO;
import br.gov.agu.samir.new_samir_back.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculoAlcadaService {


    private final TabelaCalculoService tabelaCalculoService;
    private final ResumoProcessoService resumoProcessoService;
    private final RendimentosAcumuladosIRService rendimentosAcumuladosIRService;
    private final AnaliseJEFService analiseJEFService;
    private final DateUtils dateUtils;


    public CalculadoraResponseDTO calcularComAlcada(CalculadoraRequestDTO infoCalculo) {

        CalculadoraResponseDTO responseDTO = new CalculadoraResponseDTO();


        List<LinhaTabelaDTO> tabelaComum = tabelaCalculoService.gerarTabelaCalculo(infoCalculo);

        atulizarCalculoParaAlcada(infoCalculo);
        List<LinhaTabelaDTO> tabelaAlcada = tabelaCalculoService.gerarTabelaCalculo(infoCalculo);
        responseDTO.setTabela(tabelaAlcada);

        ResumoProcessoDTO resumoProcesso = resumoProcessoService.gerarResumoProcesso(tabelaAlcada, infoCalculo.getAcordo(), BigDecimal.ZERO);
        responseDTO.setResumoProcesso(resumoProcesso);

        RendimentosAcumuladosIRDTO rendimentosAcumuladosIR = rendimentosAcumuladosIRService.getRendimentosAcumuladosIR(tabelaComum, infoCalculo.getAcordo());
        responseDTO.setRendimentosAcumuladosIR(rendimentosAcumuladosIR);

        AnaliseJuizadoEspecialFederalDTO analiseJuizadoEspecialFederal = analiseJEFService.gerarAnaliseJEF(tabelaAlcada, tabelaComum, dateUtils.mapLocalDateToString(infoCalculo.getDataAjuizamento()));
        responseDTO.setAnaliseJuizadoEspecialFederal(analiseJuizadoEspecialFederal);

        return responseDTO;

    }


    private void atulizarCalculoParaAlcada(CalculadoraRequestDTO requestDTO){
        LocalDate dataAjuizamento = requestDTO.getDataAjuizamento();
        LocalDate dataAtualizarAte = dataAjuizamento.withDayOfMonth(1);
        LocalDate fimCalculo = dataAjuizamento.withDayOfMonth(1);

        requestDTO.setDataFim(fimCalculo);
        requestDTO.setAtualizarAte(dataAtualizarAte);
    }



}
