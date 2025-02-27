package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo.CompetenciaDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo.DevidoRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo.PlanilhaDeCalculoDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.novo.PlanilhaCalculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HonorariosAdvocaticiosService {

    private final TabelaCalculoService tabelaCalculoService;
    private final PlanilhaCalculoService planilhaCalculoService;


    
    public BigDecimal calcularHonorarios(CalculoRequestDTO calculoRequestDTO) {
        
        if (calculoRequestDTO.getHonoratiosAte() == null){
            return BigDecimal.ZERO;
        }

        alterarFinalCalculoParaDataSentenca(calculoRequestDTO);

        List<PlanilhaDeCalculoDTO> planilhasCalculo = planilhaCalculoService.getPlanilhasCalculo(calculoRequestDTO);

        BigDecimal valorHonorarios = BigDecimal.ZERO;

        for (PlanilhaDeCalculoDTO planilha : planilhasCalculo){
            BigDecimal somaPrincpal = planilha.getCompetencias().stream()
                    .map(CompetenciaDTO::getSoma)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .multiply(BigDecimal.valueOf(calculoRequestDTO.getPercentualHonorarios())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

            valorHonorarios = valorHonorarios.add(somaPrincpal);
        }

        return valorHonorarios;
    }

    private static void alterarFinalCalculoParaDataSentenca(CalculoRequestDTO calculoRequestDTO) {
        for (DevidoRequestDTO calculoDevido : calculoRequestDTO.getDevidos()){
            calculoDevido.setDataFinal(calculoRequestDTO.getHonoratiosAte());
        }
    }


}
