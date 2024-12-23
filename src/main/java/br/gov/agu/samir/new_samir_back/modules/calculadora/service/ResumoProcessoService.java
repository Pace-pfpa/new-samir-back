package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.DescricaoValorDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.LinhaTabelaDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.ResumoProcessoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumoProcessoService {



    public ResumoProcessoDTO gerarResumoProcesso(List<LinhaTabelaDTO> tabela, int acordo, BigDecimal valorHonorarios) {

        ResumoProcessoDTO resumoProcesso = new ResumoProcessoDTO();
        DescricaoValorDTO porcentagem = gerarPorcentagem(acordo);
        DescricaoValorDTO somaPrincipal = gerarSomaDoPrincipal(tabela, acordo);
        DescricaoValorDTO jurosDeMora = gerarJurosDeMora(tabela, acordo);
        DescricaoValorDTO devidoAoReclamante = gerarDevidoAoReclamante(tabela, acordo);
        DescricaoValorDTO honorarios = gerarHonorariosAdvocaticios(valorHonorarios, acordo);
        DescricaoValorDTO totalProcesso = gerarTotalDoProcesso(devidoAoReclamante, honorarios, acordo);
        List<DescricaoValorDTO> descricoes = new ArrayList<>(List.of(porcentagem, somaPrincipal, jurosDeMora, devidoAoReclamante, honorarios, totalProcesso));
        resumoProcesso.setDescricoes(descricoes);
        return resumoProcesso;
    }


    private DescricaoValorDTO gerarPorcentagem(int acordo) {
        DescricaoValorDTO porcentagem = new DescricaoValorDTO();
        porcentagem.setDescricao("Porcentagem:");
        porcentagem.setTotalPeriodoCalculo("100%");
        porcentagem.setCalculoParaExecucao(acordo + "%");
        return porcentagem;
    }

    private DescricaoValorDTO gerarSomaDoPrincipal(List<LinhaTabelaDTO> tabela, int acordo) {
        DescricaoValorDTO somaPrincipal = new DescricaoValorDTO();
        BigDecimal totalPrincipal = tabela.stream().map(LinhaTabelaDTO::getDevido).reduce(BigDecimal.ZERO, BigDecimal::add);
        somaPrincipal.setDescricao("Soma do Principal:");
        somaPrincipal.setTotalPeriodoCalculo("R$ " + totalPrincipal);
        somaPrincipal.setCalculoParaExecucao("R$ " + totalPrincipal.multiply(BigDecimal.valueOf(acordo)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        return somaPrincipal;
    }

    private DescricaoValorDTO gerarJurosDeMora(List<LinhaTabelaDTO> tabela, int acordo) {
        DescricaoValorDTO jurosDeMora = new DescricaoValorDTO();
        BigDecimal totalJuros = tabela.stream().map(LinhaTabelaDTO::getJuros).reduce(BigDecimal.ZERO, BigDecimal::add);
        jurosDeMora.setDescricao("Juros de Mora:");
        jurosDeMora.setTotalPeriodoCalculo("R$ " + totalJuros);
        jurosDeMora.setCalculoParaExecucao("R$ " + totalJuros.multiply(BigDecimal.valueOf(acordo)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        return jurosDeMora;
    }

    private DescricaoValorDTO gerarDevidoAoReclamante(List<LinhaTabelaDTO> tabela, int acordo) {
        DescricaoValorDTO devidoAoReclamante = new DescricaoValorDTO();
        BigDecimal totalDevido = tabela.stream().map(LinhaTabelaDTO::getDevido).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalJuros = tabela.stream().map(LinhaTabelaDTO::getJuros).reduce(BigDecimal.ZERO, BigDecimal::add);
        devidoAoReclamante.setDescricao("Devido ao Reclamante:");
        devidoAoReclamante.setTotalPeriodoCalculo("R$ " + totalDevido.add(totalJuros));
        devidoAoReclamante.setCalculoParaExecucao("R$ " + totalDevido.add(totalJuros).multiply(BigDecimal.valueOf(acordo)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        return devidoAoReclamante;
    }

    private DescricaoValorDTO gerarHonorariosAdvocaticios(BigDecimal valorHonorarios, int acordo) {


        DescricaoValorDTO honorarios = new DescricaoValorDTO();
        honorarios.setDescricao("Honorários Advocatícios:");
        honorarios.setTotalPeriodoCalculo("R$ " + valorHonorarios);
        honorarios.setCalculoParaExecucao("R$ " + valorHonorarios.multiply(BigDecimal.valueOf(acordo)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        return honorarios;
    }

    private DescricaoValorDTO gerarTotalDoProcesso(DescricaoValorDTO devidoAoReclamante, DescricaoValorDTO honorariosAdvocaticios, int acordo) {
        BigDecimal devidoAoReclamente = new BigDecimal(devidoAoReclamante.getCalculoParaExecucao().replace("R$ ", ""));
        BigDecimal honorarios = new BigDecimal(honorariosAdvocaticios.getCalculoParaExecucao().replace("R$ ", ""));
        BigDecimal valorTotal = devidoAoReclamente.add(honorarios);
        DescricaoValorDTO totalProcesso = new DescricaoValorDTO();
        totalProcesso.setDescricao("Total do Processo:");
        totalProcesso.setTotalPeriodoCalculo("R$ " + valorTotal);
        totalProcesso.setCalculoParaExecucao("R$ " + valorTotal.multiply(BigDecimal.valueOf(acordo)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        return totalProcesso;
    }


}
