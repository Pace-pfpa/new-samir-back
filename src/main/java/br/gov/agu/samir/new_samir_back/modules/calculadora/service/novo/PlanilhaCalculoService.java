package br.gov.agu.samir.new_samir_back.modules.calculadora.service.novo;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo.DevidoRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo.PlanilhaDeCalculoDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.CompetenciaService;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.RendimentosAcumuladosIRService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanilhaCalculoService {

    private final CompetenciaService competenciaService;
    private final RendimentosAcumuladosIRService rendimentosAcumuladosIRService;


    public List<PlanilhaDeCalculoDTO> getPlanilhasCalculo(CalculoRequestDTO requestDTO) {
        List<PlanilhaDeCalculoDTO> planilhas = new ArrayList<>();

        for (DevidoRequestDTO devido : requestDTO.getDevidos()) {
            PlanilhaDeCalculoDTO planilha = gerarDetalhesDevido(devido);
            planilha.setCompetencias(competenciaService.gerarCompetencias(requestDTO,devido));
            planilha.setRendimentosRecebidos(rendimentosAcumuladosIRService.getRendimentosAcumuladosIR(planilha.getCompetencias(),requestDTO.getAcordo()));
            planilhas.add(planilha);
        }

        return planilhas;
    }


    private PlanilhaDeCalculoDTO gerarDetalhesDevido(DevidoRequestDTO devido) {
        PlanilhaDeCalculoDTO planilha = new PlanilhaDeCalculoDTO();
        planilha.setEspecie(devido.getEspecie());
        planilha.setDib(devido.getDib());
        planilha.setRmi(devido.getRmi());
        planilha.setPorcentagemRmi(devido.getPorcentagemRmi());
        String periodo = devido.getDataInicial().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " a " + devido.getDataFinal().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        planilha.setPeriodo(periodo);
        return planilha;
    }
}
