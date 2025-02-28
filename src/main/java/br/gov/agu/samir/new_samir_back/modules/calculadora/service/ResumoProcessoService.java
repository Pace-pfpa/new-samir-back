package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.*;
import br.gov.agu.samir.new_samir_back.modules.salario_minimo.service.SalarioMinimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumoProcessoService {

    private final SalarioMinimoService salarioMinimoService;
    private final HonorariosAdvocaticiosService honorariosAdvocaticiosService;


    public ResumoProcessoDTO gerarResumoProcesso(List<PlanilhaDeCalculoDTO> planilhasCalculo, CalculoRequestDTO requestDTO) {

        BigDecimal somaPrincipal = calcularSomaPrincipal(planilhasCalculo).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal somaPrincipalComAcordo = calcularSomaPrincipalComAcordo(somaPrincipal, requestDTO).setScale(2, RoundingMode.HALF_DOWN);

        BigDecimal jurosMora = calcularJurosMora(planilhasCalculo, requestDTO).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal jurosMoraComAcordo = calcularJurosMoraComAcordo(jurosMora, requestDTO).setScale(2, RoundingMode.HALF_DOWN);

        BigDecimal devidoReclamante = somaPrincipalComAcordo.add(jurosMoraComAcordo).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal devidoReclamanteComAcordo = calcularDevidoReclamanteComAcordo(devidoReclamante, requestDTO).setScale(2, RoundingMode.HALF_DOWN);

        BigDecimal honorarios = honorariosAdvocaticiosService.calcularHonorarios(requestDTO).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal honorariosComAcordo = calcularHonorariosComAcordo(honorarios, requestDTO).setScale(2, RoundingMode.HALF_DOWN);

        BigDecimal totalProcesso = devidoReclamante.add(honorarios).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal totalProcessoComAcordo = devidoReclamanteComAcordo.add(honorariosComAcordo).setScale(2, RoundingMode.HALF_DOWN);

        ValoresResumoDTO valoresOriginais = new ValoresResumoDTO(
                "100%",somaPrincipal,jurosMora,devidoReclamante,honorarios,totalProcesso
        );

        ValoresResumoDTO valoresComAcordo = new ValoresResumoDTO(
                requestDTO.getAcordo() + "%",somaPrincipalComAcordo,jurosMoraComAcordo,devidoReclamanteComAcordo,honorariosComAcordo,totalProcessoComAcordo
        );

        return new ResumoProcessoDTO(List.of(valoresOriginais,valoresComAcordo));
    }

    private BigDecimal calcularHonorariosComAcordo(BigDecimal honorarios, CalculoRequestDTO requestDTO) {
        return honorarios.multiply(BigDecimal.valueOf(requestDTO.getAcordo())
                .divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP));
    }

    private BigDecimal calcularDevidoReclamanteComAcordo(BigDecimal devidoReclamante, CalculoRequestDTO requestDTO) {
        return devidoReclamante.multiply(BigDecimal.valueOf(requestDTO.getAcordo())
                .divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP));
    }


    private BigDecimal calcularJurosMoraComAcordo(BigDecimal jurosMora, CalculoRequestDTO requestDTO) {
        return jurosMora.multiply(BigDecimal.valueOf(requestDTO.getAcordo())
                .divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP));
    }

    private BigDecimal calcularJurosMora(List<PlanilhaDeCalculoDTO> planilhasCalculo, CalculoRequestDTO requestDTO) {
        return planilhasCalculo.stream()
                .flatMap(planilha -> planilha.getCompetencias().stream())
                .map(CompetenciaDTO::getJuros)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private BigDecimal calcularSomaPrincipalComAcordo(BigDecimal somaPrincipal, CalculoRequestDTO requestDTO) {
        BigDecimal somaPrincipalCorrigida = somaPrincipal.multiply(BigDecimal.valueOf(requestDTO.getAcordo())
                .divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP));

        if (requestDTO.isLimitarAcordo()){
            BigDecimal limiteAcordo = salarioMinimoService.getSalarioMinimoProximoPorDataNoMesmoAno(LocalDate.now().withDayOfMonth(1)).multiply(BigDecimal.valueOf(60));
            return isValorAcordadoMaiorQueLimite(somaPrincipalCorrigida, limiteAcordo) ? limiteAcordo : somaPrincipalCorrigida;
        }

        return somaPrincipalCorrigida;
    }

    private boolean isValorAcordadoMaiorQueLimite(BigDecimal somaPrincipalCorrigida, BigDecimal limiteAcorodo) {
        return somaPrincipalCorrigida.compareTo(limiteAcorodo) > 0;
    }


    private BigDecimal calcularSomaPrincipal(List<PlanilhaDeCalculoDTO> planilhasCalculo) {
        return planilhasCalculo.stream()
                .flatMap(planilha -> planilha.getCompetencias().stream())
                .map(CompetenciaDTO::getSoma)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }



}
