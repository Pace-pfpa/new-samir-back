package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.DescricaoValorIRDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.LinhaTabelaDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.RendimentosAcumuladosIRDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class RendimentosAcumuladosIRService {

    public RendimentosAcumuladosIRDTO getRendimentosAcumuladosIR(List<LinhaTabelaDTO> tabela, int acordo) {

        RendimentosAcumuladosIRDTO rendimentosAcumuladosIR = new RendimentosAcumuladosIRDTO();
        DescricaoValorIRDTO anoAtual = gerarAnoCalendarioPagamento(tabela, acordo);
        DescricaoValorIRDTO anterios = gerarAnosAnteriores(tabela, acordo);
        DescricaoValorIRDTO total = gerarTotal(anoAtual, anterios, acordo);
        rendimentosAcumuladosIR.setDescricaoIR(List.of(anoAtual, anterios, total));
        return rendimentosAcumuladosIR;

    }

    private DescricaoValorIRDTO gerarAnoCalendarioPagamento(List<LinhaTabelaDTO> tabela, int acordo) {

        DescricaoValorIRDTO anoCalendarioPagamento = new DescricaoValorIRDTO();
        AtomicInteger competencias = new AtomicInteger();
        anoCalendarioPagamento.setDescricao("Ano calendário do pagamento(" + LocalDate.now().getYear() + ")");
        BigDecimal somaAnoPagamento = tabela.stream()
                .filter(linha -> linha.getData().contains(String.valueOf(LocalDate.now().getYear())))
                .map(LinhaTabelaDTO::getSoma)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        tabela.stream().filter(linha -> linha.getData().contains(String.valueOf(LocalDate.now().getYear()))).forEach(linha -> competencias.getAndIncrement());

        anoCalendarioPagamento.setValor("R$ " + somaAnoPagamento);
        anoCalendarioPagamento.setAcordo("R$ " + somaAnoPagamento.multiply(BigDecimal.valueOf(acordo)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        anoCalendarioPagamento.setCompetencias(competencias.get());

        return anoCalendarioPagamento;
    }

    private DescricaoValorIRDTO gerarAnosAnteriores(List<LinhaTabelaDTO> tabela, int acordo) {

        DescricaoValorIRDTO anosAnteriores = new DescricaoValorIRDTO();
        anosAnteriores.setDescricao("Anos anteriores");

        int anoAtual = LocalDate.now().getYear();
        AtomicInteger competencias = new AtomicInteger();

        BigDecimal somaAnosAnteriores = tabela.stream()
                .filter(linha -> !linha.getData().contains(String.valueOf(anoAtual)))
                .peek(linha -> competencias.getAndIncrement()) //
                .map(LinhaTabelaDTO::getSoma) //
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        anosAnteriores.setValor("R$ " + somaAnosAnteriores);
        anosAnteriores.setAcordo("R$ " + somaAnosAnteriores.multiply(BigDecimal.valueOf(acordo))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        anosAnteriores.setCompetencias(competencias.get());

        return anosAnteriores;
    }

    private DescricaoValorIRDTO gerarTotal(DescricaoValorIRDTO anoAtual, DescricaoValorIRDTO anterios, int acordo) {
        DescricaoValorIRDTO total = new DescricaoValorIRDTO();
        total.setDescricao("Total");
        BigDecimal valoresAnoAtual = new BigDecimal(anoAtual.getValor().replace("R$ ", ""));
        BigDecimal valoresAnosAnteriores = new BigDecimal(anterios.getValor().replace("R$ ", ""));
        BigDecimal valorTotal = valoresAnoAtual.add(valoresAnosAnteriores);
        int totalCompetencias = anoAtual.getCompetencias() + anterios.getCompetencias();
        total.setValor("R$ " + valorTotal);
        total.setAcordo("R$ " + valorTotal.multiply(BigDecimal.valueOf(acordo)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        total.setCompetencias(totalCompetencias);
        return total;
    }


}
