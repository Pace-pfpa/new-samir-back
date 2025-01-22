package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.DescricaoValorIRDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.LinhaTabelaDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.RendimentosAcumuladosIRDTO;
import br.gov.agu.samir.new_samir_back.util.DinheiroFormatador;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class RendimentosAcumuladosIRService {

    public RendimentosAcumuladosIRDTO getRendimentosAcumuladosIR(List<LinhaTabelaDTO> tabela, int acordo) {

        RendimentosAcumuladosIRDTO rendimentosAcumuladosIR = new RendimentosAcumuladosIRDTO();

        Map<BigDecimal, Integer> totalAnoPagamentoECompetencias = calcularTotalAnoPagamentoECompetencias(tabela);
        Map<BigDecimal, Integer> totalPagamentoAnosAnteriores = calcularTotalPagamentoAnosAnteriores(tabela);

        BigDecimal valorAnoPagamento = totalAnoPagamentoECompetencias.keySet().stream().findFirst().orElse(BigDecimal.ZERO);
        BigDecimal valorAnosAnteriores = totalPagamentoAnosAnteriores.keySet().stream().findFirst().orElse(BigDecimal.ZERO);
        int competenciasAnosAnterioes = totalPagamentoAnosAnteriores.values().stream().findFirst().orElse(0);
        int competenciasAnoPagamento = totalAnoPagamentoECompetencias.values().stream().findFirst().orElse(0);

        BigDecimal valorTotal = valorAnoPagamento.add(valorAnosAnteriores);
        int competenciasTotal = competenciasAnosAnterioes + competenciasAnoPagamento;

        DescricaoValorIRDTO anoCalendarioPagamento = gerarAnoCalendarioPagamento(valorAnoPagamento, competenciasAnoPagamento, acordo);
        DescricaoValorIRDTO anosAnteriores = gerarAnosAnteriores(valorAnosAnteriores, competenciasAnosAnterioes, acordo);
        DescricaoValorIRDTO total = gerarTotal(valorTotal, competenciasTotal, acordo);

        rendimentosAcumuladosIR.setDescricaoIR(List.of(anoCalendarioPagamento, anosAnteriores, total));

        return rendimentosAcumuladosIR;

    }

    private Map<BigDecimal, Integer> calcularTotalAnoPagamentoECompetencias(List<LinhaTabelaDTO> tabela) {
        AtomicInteger competencias = new AtomicInteger();
        BigDecimal somaAnoPagamento = tabela.stream()
                .filter(linha -> linha.getData().contains(String.valueOf(LocalDate.now().getYear())))
                .map(LinhaTabelaDTO::getSoma)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        tabela.stream().filter(linha -> linha.getData().contains(String.valueOf(LocalDate.now().getYear()))).forEach(linha -> competencias.getAndIncrement());

        return new HashMap<BigDecimal, Integer>() {{
            put(somaAnoPagamento, competencias.get());
        }};
    }

    private Map<BigDecimal, Integer> calcularTotalPagamentoAnosAnteriores(List<LinhaTabelaDTO> tabela) {
        AtomicInteger competencias = new AtomicInteger();
        BigDecimal somaAnosAnteriores = tabela.stream()
                .filter(linha -> !linha.getData().contains(String.valueOf(LocalDate.now().getYear())))
                .peek(linha -> competencias.getAndIncrement()) //
                .map(LinhaTabelaDTO::getSoma) //
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new HashMap<BigDecimal, Integer>() {{
            put(somaAnosAnteriores, competencias.get());
        }};
    }


    private DescricaoValorIRDTO gerarAnoCalendarioPagamento(BigDecimal valoresAnoPagamento, int competencias ,int acordo) {
        DescricaoValorIRDTO anoCalendarioPagamento = new DescricaoValorIRDTO();
        anoCalendarioPagamento.setDescricao("Ano calendário de pagamento");
        anoCalendarioPagamento.setValor(DinheiroFormatador.formatarParaReal(valoresAnoPagamento));
        BigDecimal valorAcordado = valoresAnoPagamento.multiply(BigDecimal.valueOf(acordo)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        anoCalendarioPagamento.setAcordo(DinheiroFormatador.formatarParaReal(valorAcordado));
        anoCalendarioPagamento.setCompetencias(competencias);
        return anoCalendarioPagamento;
    }

    private DescricaoValorIRDTO gerarAnosAnteriores(BigDecimal valorAnosAnterios,int competenciasAnosAnteriores, int acordo) {

        DescricaoValorIRDTO anosAnteriores = new DescricaoValorIRDTO();
        anosAnteriores.setDescricao("Anos anteriores");
        anosAnteriores.setValor(DinheiroFormatador.formatarParaReal(valorAnosAnterios));
        BigDecimal valorAcordado = valorAnosAnterios.multiply(BigDecimal.valueOf(acordo)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        anosAnteriores.setAcordo(DinheiroFormatador.formatarParaReal(valorAcordado));
        anosAnteriores.setCompetencias(competenciasAnosAnteriores);

        return anosAnteriores;
    }

    private DescricaoValorIRDTO gerarTotal(BigDecimal valorTotal, int competenciasTotal, int acordo) {

        DescricaoValorIRDTO total = new DescricaoValorIRDTO();
        total.setDescricao("Total");
        total.setValor(DinheiroFormatador.formatarParaReal(valorTotal));
        BigDecimal valorAcordado = valorTotal.multiply(BigDecimal.valueOf(acordo)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        total.setAcordo(DinheiroFormatador.formatarParaReal(valorAcordado));
        total.setCompetencias(competenciasTotal);

        return total;
    }


}
