package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.AnaliseJuizadoEspecialFederalDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.DescricaoAnaliseJEFDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.LinhaTabelaDTO;
import br.gov.agu.samir.new_samir_back.modules.salario_minimo.service.SalarioMinimoService;
import br.gov.agu.samir.new_samir_back.util.DinheiroFormatador;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnaliseJEFService {

    private final SalarioMinimoService salarioMinimoService;


    //TODO REFATORAR MÉTODO
    public AnaliseJuizadoEspecialFederalDTO gerarAnaliseJEF(List<LinhaTabelaDTO> tabelaAlcada, List<LinhaTabelaDTO> tabelaNormal, String dataAjuizamento) {
        List<DescricaoAnaliseJEFDTO> analiseJEF = new ArrayList<>();

        BigDecimal valorParcelasDevidasAteAjuizamento = calcularValorParcelasDevidasAteAjuizamento(tabelaAlcada);
        BigDecimal valor12ParcelasVicendas = calcularValor12ParcelasVicendas(tabelaAlcada);
        BigDecimal valorCausaNoAjuizamento = valorParcelasDevidasAteAjuizamento.add(valor12ParcelasVicendas);
        BigDecimal salarioMinimo = salarioMinimoService.getSalarioMinimoProximoPorDataNoMesmoAno(LocalDate.parse(dataAjuizamento, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        BigDecimal limite60SalariosMinimos = calcularLimite60SalariosMinimos(salarioMinimo);
        BigDecimal valorRenunciaJEFAtualizado = calcularValorAtualizadoRenuncia(tabelaNormal, valorCausaNoAjuizamento, limite60SalariosMinimos, dataAjuizamento);

        analiseJEF.add(gerarParcelasDevidasAteAjuizameto(valorParcelasDevidasAteAjuizamento, dataAjuizamento));
        analiseJEF.add(gerarAnalise12ParcelasVicendas(valor12ParcelasVicendas));
        analiseJEF.add(valorDaCausaNoAjuizamento(valorCausaNoAjuizamento));
        analiseJEF.add(limite60SalariosMinimos(limite60SalariosMinimos));
        analiseJEF.add(gerarRenunciaJEF(valorRenunciaJEFAtualizado));
        return new AnaliseJuizadoEspecialFederalDTO(analiseJEF);
    }

    private BigDecimal calcularValorAtualizadoRenuncia(List<LinhaTabelaDTO> tabelaNormal, BigDecimal valorCausaAjuizamento, BigDecimal limite60SalariosMinimos, String dataAjuizamento) {
        BigDecimal valorRenunciaJEF = valorCausaAjuizamento.subtract(limite60SalariosMinimos);
        String mesAnoAjuizamento = dataAjuizamento.substring(3, 10);
        BigDecimal correcaoAjuizamento = tabelaNormal.stream().filter(linha -> linha.getData().contains(mesAnoAjuizamento)).map(LinhaTabelaDTO::getIndiceCorrecaoMonetaria).findFirst().orElse(BigDecimal.ZERO);
        BigDecimal jurosAjuizamento = tabelaNormal.stream().filter(linha -> linha.getData().contains(mesAnoAjuizamento)).map(LinhaTabelaDTO::getPorcentagemJuros).findFirst().orElse(BigDecimal.ZERO);
        BigDecimal valorAtualizadoRenuncia = valorRenunciaJEF.multiply(correcaoAjuizamento).add(valorRenunciaJEF.multiply(jurosAjuizamento));
        return valorAtualizadoRenuncia;
    }

    private BigDecimal calcularLimite60SalariosMinimos(BigDecimal salarioMinimo) {
        return salarioMinimo.multiply(BigDecimal.valueOf(60));
    }

    private BigDecimal calcularValorParcelasDevidasAteAjuizamento(List<LinhaTabelaDTO> tabelaAlcada) {
        return tabelaAlcada.stream().map(LinhaTabelaDTO::getSoma).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularValor12ParcelasVicendas(List<LinhaTabelaDTO> tabelaAlcada) {
        int ultimoIndex = tabelaAlcada.size() - 1;
        BigDecimal valorParcela = tabelaAlcada.get(ultimoIndex).getSoma();
        return valorParcela.multiply(BigDecimal.valueOf(12));
    }


    private DescricaoAnaliseJEFDTO gerarParcelasDevidasAteAjuizameto(BigDecimal valorParcelasDevidasAteAjuizamento, String dataAjuizamento) {
        DescricaoAnaliseJEFDTO descricao = new DescricaoAnaliseJEFDTO();
        descricao.setDescricao("Parcelas devidas até ajuizamento " + dataAjuizamento);
        descricao.setValor(DinheiroFormatador.formatarParaReal(valorParcelasDevidasAteAjuizamento));
        return descricao;
    }

    private DescricaoAnaliseJEFDTO gerarAnalise12ParcelasVicendas(BigDecimal valor12ParcelasVicendas) {
        DescricaoAnaliseJEFDTO descricao = new DescricaoAnaliseJEFDTO();
        descricao.setDescricao("12 parcelas vincendas");
        descricao.setValor(DinheiroFormatador.formatarParaReal(valor12ParcelasVicendas));
        return descricao;
    }

    private DescricaoAnaliseJEFDTO valorDaCausaNoAjuizamento(BigDecimal valorCausaNoAjuizamento) {
        DescricaoAnaliseJEFDTO descricao = new DescricaoAnaliseJEFDTO();
        descricao.setDescricao("Valor da causa no ajuizamento");
        descricao.setValor(DinheiroFormatador.formatarParaReal(valorCausaNoAjuizamento));
        return descricao;
    }

    private DescricaoAnaliseJEFDTO limite60SalariosMinimos(BigDecimal valor60SalariosMinimos) {
        DescricaoAnaliseJEFDTO descricao = new DescricaoAnaliseJEFDTO();
        descricao.setDescricao("Limite 60 salários mínimos");
        descricao.setValor(DinheiroFormatador.formatarParaReal(valor60SalariosMinimos));
        return descricao;
    }

    private DescricaoAnaliseJEFDTO gerarRenunciaJEF(BigDecimal valorRenunciaAtualizado) {
        DescricaoAnaliseJEFDTO descricao = new DescricaoAnaliseJEFDTO();
        descricao.setDescricao("(*)Valor atualizado da renúncia pela alçada do JEF:");
        descricao.setValor(DinheiroFormatador.formatarParaReal(valorRenunciaAtualizado));
        return descricao;
    }

}

