package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.CalculadoraRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.LinhaTabelaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class HonorariosAdvocaticiosService {

    private final TabelaCalculoService tabelaCalculoService;


    @Async
    public CompletableFuture<BigDecimal> calcularHonorarios(CalculadoraRequestDTO infoCalculo) {
        List<LinhaTabelaDTO> tabelaCalculo = tabelaCalculoService.gerarTabelaCalculo(infoCalculo);
        BigDecimal totalProcesso = tabelaCalculo.stream().map(LinhaTabelaDTO::getSoma).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal resultado = totalProcesso.multiply(BigDecimal.valueOf(infoCalculo.getPorcentagemHonorarios())).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return CompletableFuture.completedFuture(resultado);
    }


}
