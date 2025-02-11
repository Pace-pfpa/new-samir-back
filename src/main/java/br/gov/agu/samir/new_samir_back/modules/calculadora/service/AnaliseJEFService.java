package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.beneficio.enums.BeneficiosEnum;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.AnaliseJuizadoEspecialFederalDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.CalculadoraRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.DescricaoAnaliseJEFDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.LinhaTabelaDTO;
import br.gov.agu.samir.new_samir_back.modules.salario_minimo.service.SalarioMinimoService;
import br.gov.agu.samir.new_samir_back.util.DinheiroFormatador;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@RequiredArgsConstructor
public class AnaliseJEFService {

    private final SalarioMinimoService salarioMinimoService;


    private static final Set<BeneficiosEnum> BENEFICIOS_QUE_RECEBEM_12_PARCELAS_VICENDAS= EnumSet.of(
            BeneficiosEnum.APOSENTADORIA_POR_TEMPO_DE_CONTRIBUICAO,
            BeneficiosEnum.APOSENTADORIA_POR_IDADE,
            BeneficiosEnum.APOSENTADORIA_POR_INVALIDEZ,
            BeneficiosEnum.PENSAO_POR_MORTE,
            BeneficiosEnum.LOAS_DEFICIENTE,
            BeneficiosEnum.LOAS_IDOSO
    );

    private boolean isBeneficioQueRecebe12ParcelasVicendas(BeneficiosEnum beneficio) {
        return BENEFICIOS_QUE_RECEBEM_12_PARCELAS_VICENDAS.contains(beneficio);
    }

    public AnaliseJuizadoEspecialFederalDTO gerarAnaliseJEF(List<LinhaTabelaDTO> tabelaAlcada, List<LinhaTabelaDTO> tabelaNormal, CalculadoraRequestDTO infoRequestDTO) {
        List<DescricaoAnaliseJEFDTO> analiseJEF = new ArrayList<>();

        BigDecimal valorParcelasDevidasAteAjuizamento = calcularValorParcelasDevidasAteAjuizamento(tabelaAlcada);
        BigDecimal valor12ParcelasVicendas = calcularValor12ParcelasVicendas(tabelaAlcada,infoRequestDTO);
        BigDecimal valorCausaNoAjuizamento = valorParcelasDevidasAteAjuizamento.add(valor12ParcelasVicendas);
        BigDecimal salarioMinimo = salarioMinimoService.getSalarioMinimoProximoPorDataNoMesmoAno(infoRequestDTO.getDataAjuizamento());
        BigDecimal limite60SalariosMinimos = calcularLimite60SalariosMinimos(salarioMinimo);
        BigDecimal valorRenunciaJEFAtualizado = calcularValorAtualizadoRenuncia(tabelaNormal, valorCausaNoAjuizamento, limite60SalariosMinimos, infoRequestDTO.getDataAjuizamento());

        analiseJEF.add(gerarSalarioMinimoAnaliseJEF(salarioMinimo,infoRequestDTO.getDataAjuizamento().getYear()));
        analiseJEF.add(gerarParcelasDevidasAteAjuizameto(valorParcelasDevidasAteAjuizamento, infoRequestDTO.getDataAjuizamento()));
        analiseJEF.add(gerarAnalise12ParcelasVicendas(valor12ParcelasVicendas));
        analiseJEF.add(valorDaCausaNoAjuizamento(valorCausaNoAjuizamento));
        analiseJEF.add(limite60SalariosMinimos(limite60SalariosMinimos));
        analiseJEF.add(gerarRenunciaJEF(valorRenunciaJEFAtualizado));
        return new AnaliseJuizadoEspecialFederalDTO(analiseJEF);
    }

    private DescricaoAnaliseJEFDTO gerarSalarioMinimoAnaliseJEF(BigDecimal salarioMinimo, int anoSalarioMinimo) {
        DescricaoAnaliseJEFDTO descricao = new DescricaoAnaliseJEFDTO();
        descricao.setDescricao("Salário mínimo (" + anoSalarioMinimo + ")");
        descricao.setValor(DinheiroFormatador.formatarParaReal(salarioMinimo));
        return descricao;
    }

    private BigDecimal calcularValorAtualizadoRenuncia(List<LinhaTabelaDTO> tabelaNormal, BigDecimal valorCausaAjuizamento, BigDecimal limite60SalariosMinimos, LocalDate dataAjuizamento) {
        BigDecimal valorRenunciaJEF = valorCausaAjuizamento.subtract(limite60SalariosMinimos);
        String dataAjuizamentoString = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dataAjuizamento);
        String mesAnoAjuizamento = dataAjuizamentoString.substring(3);
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

    private BigDecimal calcularValor12ParcelasVicendas(List<LinhaTabelaDTO> tabelaAlcada, CalculadoraRequestDTO infoRequestDTO) {
        int ultimoIndex = tabelaAlcada.size() - 1;
        BigDecimal valorParcela = tabelaAlcada.get(ultimoIndex).getSoma();
        BeneficiosEnum beneficio = BeneficiosEnum.getByNome(infoRequestDTO.getBeneficio());
        if (isBeneficioQueRecebe12ParcelasVicendas(beneficio)) {
            return valorParcela.multiply(BigDecimal.valueOf(12));
        } else {
            return BigDecimal.ZERO;
        }
    }


    private DescricaoAnaliseJEFDTO gerarParcelasDevidasAteAjuizameto(BigDecimal valorParcelasDevidasAteAjuizamento, LocalDate dataAjuizamento) {
        DescricaoAnaliseJEFDTO descricao = new DescricaoAnaliseJEFDTO();
        descricao.setDescricao("Parcelas devidas até ajuizamento " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dataAjuizamento));
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

    //TODO (para as espécies B42, 41, 32, 21, 87, 88 sempre serão 12 parcelas(o valor da competência do ajuizamento multiplicada por 12). Para a espécie B31, o cálculo das vincendas deverá observar o período entre o ajuizamento e o termo final do cálculo. Caso este período seja inferior a 12 meses, as vincendas serão calculadas conforme o número de meses. Exemplo: B31 com ação ajuizada em 01/02/2024 e termo final do cálculo em 30/06/2024, nesse caso consideráriamos 5 vincendas.
}

