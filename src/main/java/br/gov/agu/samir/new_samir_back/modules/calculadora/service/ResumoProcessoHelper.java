package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.CalculadoraRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.LinhaTabelaDTO;
import br.gov.agu.samir.new_samir_back.modules.salario_minimo.service.SalarioMinimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResumoProcessoHelper {

    private final SalarioMinimoService salarioMinimoService;
    private final HonorariosAdvocaticiosService honorariosAdvocaticiosService;


    public Map<String, BigDecimal> gerarResumoProcesso(List<LinhaTabelaDTO> tabela, CalculadoraRequestDTO request){

        BigDecimal somaPrincipal = calcularSomaDoPrincipal(tabela);
        BigDecimal somaPrincipalComAcordo = calcularSomaDoPrincipalComAcordo(somaPrincipal, request);

        BigDecimal jurosDeMora = calcularJurosDeMora(tabela);
        BigDecimal jurosDeMoraComAcordo = calcularJurosDeMoraComJuros(jurosDeMora,request);

        BigDecimal devidoAoReclamante = somaPrincipal.add(jurosDeMora);
        BigDecimal devidoAoReclamanteComAcordo = somaPrincipalComAcordo.add(jurosDeMoraComAcordo);

        BigDecimal honorariosAdvocaticios = honorariosAdvocaticiosService.calcularHonorarios(request).join();
        BigDecimal honorariosAdvocaticiosComAcordo = calcularHonorariosAdvocaticiosComAcorodo(honorariosAdvocaticios, request);

       return Map.of(
               "principal", somaPrincipal,
               "principalComAcordo", somaPrincipalComAcordo,
               "jurosDeMora", jurosDeMora,
               "jurosDeMoraComAcordo", jurosDeMoraComAcordo,
               "devidoAoReclamante", devidoAoReclamante,
               "devidoAoReclamanteComAcordo", devidoAoReclamanteComAcordo,
               "honorariosAdvocaticios", honorariosAdvocaticios,
               "honorariosAdvocaticiosComAcordo", honorariosAdvocaticiosComAcordo
       );
    }

    private BigDecimal calcularSomaDoPrincipal(List<LinhaTabelaDTO> tabela) {
        return tabela.stream()
                .map(LinhaTabelaDTO::getSoma)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    private BigDecimal calcularSomaDoPrincipalComAcordo(BigDecimal somaPrincipal, CalculadoraRequestDTO request) {
        BigDecimal principalComAcordo = somaPrincipal.multiply(BigDecimal.valueOf(request.getAcordo()))
                .divide(BigDecimal.valueOf(100),RoundingMode.HALF_UP);

        if (request.isLimitarAcordoEm60SM() && request.getAcordo() < 100){
            BigDecimal limite60SM = salarioMinimoService
                    .getSalarioMinimoProximoPorDataNoMesmoAno(LocalDate.now().withDayOfMonth(1))
                    .multiply(BigDecimal.valueOf(60)).setScale(2, RoundingMode.UNNECESSARY);

            return isAcordoSuperiorAoLimite(principalComAcordo,limite60SM) ? limite60SM : principalComAcordo;
        }
        return principalComAcordo;
    }

    private static boolean isAcordoSuperiorAoLimite(BigDecimal valorAcordado, BigDecimal valorLimite){
        return valorLimite.compareTo(valorAcordado) > 0;
    }

    private BigDecimal calcularJurosDeMora(List<LinhaTabelaDTO> tabela) {
        return tabela.stream().map(LinhaTabelaDTO::getJuros).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    private BigDecimal calcularJurosDeMoraComJuros(BigDecimal jurosDeMora, CalculadoraRequestDTO request) {
        return jurosDeMora.multiply(BigDecimal.valueOf(request.getAcordo()))
                .multiply(BigDecimal.valueOf(100));
    }

    private BigDecimal calcularHonorariosAdvocaticiosComAcorodo(BigDecimal honorariosAdvocaticios, CalculadoraRequestDTO request) {
        return honorariosAdvocaticios.multiply(BigDecimal.valueOf(request.getAcordo()))
                .divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP);
    }


}
