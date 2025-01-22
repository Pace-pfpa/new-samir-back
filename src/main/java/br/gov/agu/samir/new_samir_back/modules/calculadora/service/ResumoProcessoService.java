package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.DescricaoValorDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.LinhaTabelaDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.ResumoProcessoDTO;
import br.gov.agu.samir.new_samir_back.util.DinheiroFormatador;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumoProcessoService {


    public ResumoProcessoDTO gerarResumoProcesso(List<LinhaTabelaDTO> tabela, int acordo, BigDecimal valorHonorarios) {
        BigDecimal valorSomaPrincipal = calcularSomaDoPrincipal(tabela);
        BigDecimal valorJurosDeMora = calcularJurosDeMora(tabela);
        BigDecimal devidoAoReclamante = valorSomaPrincipal.add(valorJurosDeMora);
        BigDecimal valorTotalProcesso = devidoAoReclamante.add(valorHonorarios);


        return new ResumoProcessoDTO(
                List.of(
                        gerarPorcentagem(acordo),
                        gerarSomaDoPrincipal(valorSomaPrincipal, acordo),
                        gerarJurosDeMora(valorJurosDeMora, acordo),
                        gerarDevidoAoReclamante(devidoAoReclamante, acordo),
                        gerarHonorariosAdvocaticios(valorHonorarios, acordo),
                        gerarTotalDoProcesso(valorTotalProcesso, acordo)
                )
        );
    }

    private BigDecimal calcularSomaDoPrincipal(List<LinhaTabelaDTO> tabela) {
        return tabela.stream().map(LinhaTabelaDTO::getSoma).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularJurosDeMora(List<LinhaTabelaDTO> tabela) {
        return tabela.stream().map(LinhaTabelaDTO::getJuros).reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private DescricaoValorDTO gerarPorcentagem(int acordo) {
        DescricaoValorDTO porcentagem = new DescricaoValorDTO();
        porcentagem.setDescricao("Porcentagem:");
        porcentagem.setTotalPeriodoCalculo("100%");
        porcentagem.setCalculoParaExecucao(acordo + "%");
        return porcentagem;
    }

    private DescricaoValorDTO gerarSomaDoPrincipal(BigDecimal somaPrincial, int acordo) {
        DescricaoValorDTO descricaoSomaPrincipal = new DescricaoValorDTO();
        descricaoSomaPrincipal.setDescricao("Soma do Principal:");
        descricaoSomaPrincipal.setTotalPeriodoCalculo(DinheiroFormatador.formatarParaReal(somaPrincial));
        BigDecimal somaPrincipalComAcordo = somaPrincial.multiply(BigDecimal.valueOf(acordo)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        descricaoSomaPrincipal.setCalculoParaExecucao(DinheiroFormatador.formatarParaReal(somaPrincipalComAcordo));

        return descricaoSomaPrincipal;
    }

    private DescricaoValorDTO gerarJurosDeMora(BigDecimal jurosDeMora, int acordo) {
        DescricaoValorDTO descricaoJurosDeMora = new DescricaoValorDTO();
        descricaoJurosDeMora.setDescricao("Juros de Mora:");
        descricaoJurosDeMora.setTotalPeriodoCalculo(DinheiroFormatador.formatarParaReal(jurosDeMora));
        BigDecimal jurosDeMoraComAcordo = jurosDeMora.multiply(BigDecimal.valueOf(acordo)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        descricaoJurosDeMora.setCalculoParaExecucao(DinheiroFormatador.formatarParaReal(jurosDeMoraComAcordo));

        return descricaoJurosDeMora;
    }

    private DescricaoValorDTO gerarDevidoAoReclamante(BigDecimal devidoAoReclamante, int acordo) {
        DescricaoValorDTO devidoAoReclamanteDTO = new DescricaoValorDTO();
        devidoAoReclamanteDTO.setDescricao("Devido ao Reclamante:");
        devidoAoReclamanteDTO.setTotalPeriodoCalculo(DinheiroFormatador.formatarParaReal(devidoAoReclamante));
        BigDecimal devidoAoReclamanteComAcordo = devidoAoReclamante.multiply(BigDecimal.valueOf(acordo)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        devidoAoReclamanteDTO.setCalculoParaExecucao(DinheiroFormatador.formatarParaReal(devidoAoReclamanteComAcordo));
        return devidoAoReclamanteDTO;
    }

    private DescricaoValorDTO gerarHonorariosAdvocaticios(BigDecimal valorHonorarios, int acordo) {

        DescricaoValorDTO honorarios = new DescricaoValorDTO();
        honorarios.setDescricao("Honorários Advocatícios:");
        honorarios.setTotalPeriodoCalculo(DinheiroFormatador.formatarParaReal(valorHonorarios));
        BigDecimal valorHonorariosComAcordo = valorHonorarios.multiply(BigDecimal.valueOf(acordo)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        honorarios.setCalculoParaExecucao(DinheiroFormatador.formatarParaReal(valorHonorariosComAcordo));
        return honorarios;
    }

    private DescricaoValorDTO gerarTotalDoProcesso(BigDecimal totalProcesso, int acordo) {
        DescricaoValorDTO descricaTotalProcesso = new DescricaoValorDTO();
        descricaTotalProcesso.setDescricao("Total do Processo:");
        descricaTotalProcesso.setTotalPeriodoCalculo(DinheiroFormatador.formatarParaReal(totalProcesso));
        BigDecimal totalProcessoComAcordo = totalProcesso.multiply(BigDecimal.valueOf(acordo)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        descricaTotalProcesso.setCalculoParaExecucao(DinheiroFormatador.formatarParaReal(totalProcessoComAcordo));
        return descricaTotalProcesso;

    }


}
