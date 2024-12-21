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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnaliseJEFService {

    private final DinheiroFormatador dinheiroFormatador;
    private final SalarioMinimoService salarioMinimoService;

    public AnaliseJuizadoEspecialFederalDTO gerarAnaliseJEF(List<LinhaTabelaDTO> tabelaAlcada,List<LinhaTabelaDTO> tabelaNormal, String dataAjuizamento) {
        List<DescricaoAnaliseJEFDTO> analiseJEF = new ArrayList<>();

        DescricaoAnaliseJEFDTO parcelasDevidasAteAjuizamento = gerarParcelasDevidasAteAjuizameto(tabelaAlcada, dataAjuizamento);
        analiseJEF.add(parcelasDevidasAteAjuizamento);

        DescricaoAnaliseJEFDTO dozeParcelasVicendas = gerar12ParcelasVicendas(tabelaAlcada);
        analiseJEF.add(dozeParcelasVicendas);

        BigDecimal valor12ParcelasVicendas = dinheiroFormatador.formatarParaBigDecimal(dozeParcelasVicendas.getValor());
        BigDecimal valorParcelasDevidasAteAjuizamento = dinheiroFormatador.formatarParaBigDecimal(parcelasDevidasAteAjuizamento.getValor());

        DescricaoAnaliseJEFDTO valorDaCausaNoAjuizamento = valorDaCausaNoAjuizamento(valor12ParcelasVicendas, valorParcelasDevidasAteAjuizamento);
        analiseJEF.add(valorDaCausaNoAjuizamento);

        DescricaoAnaliseJEFDTO limite60SalariosMinimos = limite60SalariosMinimos(LocalDate.parse(dataAjuizamento));
        analiseJEF.add(limite60SalariosMinimos);

        BigDecimal valor60SalariosMinimos = dinheiroFormatador.formatarParaBigDecimal(limite60SalariosMinimos.getValor());

        DescricaoAnaliseJEFDTO renunciaJEF = gerarRenunciaJEF(valorParcelasDevidasAteAjuizamento,valor60SalariosMinimos);
        analiseJEF.add(renunciaJEF);

        DescricaoAnaliseJEFDTO valorAtualizadoRenunciaJEF = gerarValorAtualizadoRenunciaJEF(dinheiroFormatador.formatarParaBigDecimal(renunciaJEF.getValor()), tabelaNormal, dataAjuizamento);
        analiseJEF.add(valorAtualizadoRenunciaJEF);

        return new AnaliseJuizadoEspecialFederalDTO(analiseJEF);
    }


    private DescricaoAnaliseJEFDTO gerarParcelasDevidasAteAjuizameto(List<LinhaTabelaDTO> tabelaAlcada, String dataAjuizamento) {
        DescricaoAnaliseJEFDTO descricao = new DescricaoAnaliseJEFDTO();
        BigDecimal valor = tabelaAlcada.stream().map(LinhaTabelaDTO::getSoma).reduce(BigDecimal.ZERO, BigDecimal::add);
        descricao.setDescricao("Parcelas devidas até ajuizamento " + dataAjuizamento);
        descricao.setValor(dinheiroFormatador.formatarParaReal(valor));
        return descricao;
    }

    private DescricaoAnaliseJEFDTO gerar12ParcelasVicendas(List<LinhaTabelaDTO> tabelaAlcada) {
        DescricaoAnaliseJEFDTO descricao = new DescricaoAnaliseJEFDTO();
        int ultimoIndex = tabelaAlcada.size() - 1;
        BigDecimal dozeParcelasVincendas = tabelaAlcada.get(ultimoIndex).getSoma().multiply(BigDecimal.valueOf(12));
        descricao.setDescricao("12 parcelas vincendas");
        descricao.setValor(dinheiroFormatador.formatarParaReal(dozeParcelasVincendas));
        return descricao;
    }

    private DescricaoAnaliseJEFDTO valorDaCausaNoAjuizamento(BigDecimal valor12ParcelasVicendas, BigDecimal valorParcelasDevidasAteAjuizamento){
        DescricaoAnaliseJEFDTO descricao = new DescricaoAnaliseJEFDTO();
        BigDecimal valor = valor12ParcelasVicendas.add(valorParcelasDevidasAteAjuizamento);
        descricao.setDescricao("Valor da causa no ajuizamento");
        descricao.setValor(dinheiroFormatador.formatarParaReal(valor));
        return descricao;
    }

    private DescricaoAnaliseJEFDTO limite60SalariosMinimos(LocalDate dataAjuizamento){
        BigDecimal valor60SalariosMinimos = salarioMinimoService.getSalarioMinimoProximoPorDataNoMesmoAno(dataAjuizamento).multiply(BigDecimal.valueOf(60));
        DescricaoAnaliseJEFDTO descricao = new DescricaoAnaliseJEFDTO();
        descricao.setDescricao("Limite 60 salários mínimos");
        descricao.setValor(dinheiroFormatador.formatarParaReal(valor60SalariosMinimos));
        return descricao;
    }

    private DescricaoAnaliseJEFDTO gerarRenunciaJEF(BigDecimal valorDaCausaNoAjuizamento, BigDecimal limite60SalariosMinimos){
        DescricaoAnaliseJEFDTO descricao = new DescricaoAnaliseJEFDTO();
        BigDecimal valor = valorDaCausaNoAjuizamento.subtract(limite60SalariosMinimos);
        descricao.setDescricao("Parcela referente à renúncia pela alçada do JEF no ajuizamento:");
        descricao.setValor(dinheiroFormatador.formatarParaReal(valor));
        return descricao;
    }

    private DescricaoAnaliseJEFDTO gerarValorAtualizadoRenunciaJEF(BigDecimal valorRenunciaJEF, List<LinhaTabelaDTO> tabelaComum, String dataAjuizamento){

        String mesEAno = dataAjuizamento.substring(0,7);

        BigDecimal indiceCorrecaoAjuizamento =tabelaComum
                .stream()
                .filter(linha -> linha.getData().contains(mesEAno))
                .map(LinhaTabelaDTO::getIndiceCorrecaoMonetaria)
                .findFirst()
                .orElse(BigDecimal.ZERO);

        BigDecimal porcentagemJuros = tabelaComum
                .stream()
                .filter(linha -> linha.getData().contains(mesEAno))
                .map(LinhaTabelaDTO::getPorcentagemJuros)
                .findFirst()
                .orElse(BigDecimal.ZERO);

        BigDecimal valorAtualizado = valorRenunciaJEF.multiply(indiceCorrecaoAjuizamento).add(valorRenunciaJEF.multiply(porcentagemJuros));
        DescricaoAnaliseJEFDTO descricao = new DescricaoAnaliseJEFDTO();
        descricao.setDescricao("Valor atualizado da renúncia pela alçada do JEF no ajuizamento:");
        descricao.setValor(dinheiroFormatador.formatarParaReal(valorAtualizado));
        return descricao;
    }



}
