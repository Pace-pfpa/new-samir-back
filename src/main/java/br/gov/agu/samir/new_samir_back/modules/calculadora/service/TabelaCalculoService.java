package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.beneficio.dto.BeneficioAcumuladoRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.beneficio.enums.BeneficiosEnum;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.CalculadoraRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.LinhaTabelaDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.enums.TipoCorrecaoMonetaria;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.CorrecaoMonetariaFactory;
import br.gov.agu.samir.new_samir_back.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TabelaCalculoService {

    private final RmiService rmiService;
    private final DecimoTerceiroService decimoTerceiroService;
    private final CalculoJurosService calculoJurosService;
    private final CalculoIndiceReajusteService calculoIndiceReajusteService;
    private final GerarListaDatasService gerarListaDatasService;

    private final CorrecaoMonetariaFactory correcaoMonetariaFactory;

    private final DateUtils dateUtils;

    private static final String MES_DECIMO_TERCEIRO = "13";


    public List<LinhaTabelaDTO> gerarTabelaCalculo(CalculadoraRequestDTO infoCalculo) {
        BeneficiosEnum beneficioVigente = BeneficiosEnum.getByNome(infoCalculo.getBeneficio());

        LocalDate dib = infoCalculo.getDib();

        LocalDate inicioCalculo = infoCalculo.getDataInicio();

        LocalDate fimCalculo = infoCalculo.getDataFim();

        LocalDate dibAnterior = infoCalculo.getDibAnterior();

        LocalDate atualizarAte = infoCalculo.getAtualizarAte();

        BigDecimal rmi = rmiService.reajustarRmi(infoCalculo);

        rmi = retornaSalarioMinimoSeRmiForInferior(rmi, dib, beneficioVigente);

        TipoCorrecaoMonetaria tipoCorrecao = TipoCorrecaoMonetaria.getTipoCorrecaoMonetaria(infoCalculo.getTipoCorrecao());

        boolean decimoTerceiroFinalCalculo = infoCalculo.isDecimoTerceiroFinalCalculo();

        List<LinhaTabelaDTO> tabelaCalculo = new ArrayList<>();

        List<String> listaDeDatasParaCalculo = gerarDatasPorBeneficioEPeriodo(beneficioVigente,decimoTerceiroFinalCalculo, inicioCalculo, fimCalculo);

        BigDecimal indiceReajuste;

        HashSet<FiltroRecebido> listaDeCalculoRecebido = gerarListaDeCalculoParaRecebido(infoCalculo.getBeneficioAcumulados());

        for (String data : listaDeDatasParaCalculo) {
            LinhaTabelaDTO linhaTabela = new LinhaTabelaDTO();
            linhaTabela.setData(data);
            //Converter a data para LocalDate
            LocalDate dataCalculo = dateUtils.mapStringToLocalDate(data);

            if (isDataDeReajuste(dataCalculo, inicioCalculo)) {
                indiceReajuste = retornaIndiceReajuste(dataCalculo, dib, dibAnterior);
                rmi = rmi.multiply(indiceReajuste).setScale(2, RoundingMode.HALF_UP);
            }

            BigDecimal indiceReajusteDevido = retornaIndiceReajuste(dataCalculo, dib, dibAnterior);
            linhaTabela.setIndiceReajusteDevido(indiceReajusteDevido.setScale(4, RoundingMode.HALF_UP));

            BigDecimal devido = isDecimoTerceiro(data) ? retornaValorDecimoTerceiro(data, inicioCalculo,fimCalculo, rmi) : calcularValorDevido(dataCalculo,inicioCalculo,fimCalculo, rmi);
            linhaTabela.setDevido(devido.setScale(2, RoundingMode.HALF_UP));

            BigDecimal indiceReajusteRecebido = retornaIndiceReajusteRecebido(data, listaDeCalculoRecebido);
            linhaTabela.setIndiceReajusteRecebido(indiceReajusteRecebido.setScale(4, RoundingMode.HALF_UP));

            BigDecimal recebido = calcularDesconto(data, listaDeCalculoRecebido);
            linhaTabela.setRecebido(recebido.setScale(2, RoundingMode.HALF_UP));

            BigDecimal diferenca = linhaTabela.getDevido().subtract(linhaTabela.getRecebido());
            linhaTabela.setDiferenca(diferenca.setScale(2, RoundingMode.HALF_UP));

            BigDecimal correcaoMonetaria = calcularCorrecaoMonetariaPorTipo(tipoCorrecao, dataCalculo, atualizarAte);
            linhaTabela.setIndiceCorrecaoMonetaria(correcaoMonetaria.setScale(4, RoundingMode.HALF_UP));

            BigDecimal salarioCorrigido = linhaTabela.getDiferenca().multiply(linhaTabela.getIndiceCorrecaoMonetaria());
            linhaTabela.setSalarioCorrigido(salarioCorrigido.setScale(2, RoundingMode.HALF_UP));

            BigDecimal jurosPorcentagem = retornaCalculoJurosPorTipo(dataCalculo, infoCalculo);
            linhaTabela.setPorcentagemJuros(jurosPorcentagem.setScale(4, RoundingMode.HALF_UP));

            BigDecimal juros = jurosPorcentagem.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
            juros = juros.multiply(linhaTabela.getSalarioCorrigido());
            linhaTabela.setJuros(juros.setScale(2, RoundingMode.HALF_UP));

            BigDecimal soma = linhaTabela.getSalarioCorrigido().add(linhaTabela.getJuros());
            linhaTabela.setSoma(soma.setScale(2, RoundingMode.HALF_UP));

            tabelaCalculo.add(linhaTabela);
        }
        return tabelaCalculo;
    }


    //TODO: Refatorar Calculo do recebido
    private HashSet<FiltroRecebido> gerarListaDeCalculoParaRecebido(List<BeneficioAcumuladoRequestDTO> beneficiosInaculaveis) {
        HashSet<FiltroRecebido> listaDeCalculo = new HashSet<>();

        for (BeneficioAcumuladoRequestDTO beneficioInacumulavel : beneficiosInaculaveis) {

            BeneficiosEnum beneficio = BeneficiosEnum.getByNome(beneficioInacumulavel.getBeneficio());

            LocalDate dib = beneficioInacumulavel.getDib();

            LocalDate inicioDesconto = beneficioInacumulavel.getInicioDesconto();

            LocalDate dataFim = beneficioInacumulavel.getFimDesconto();

            LocalDate dataDibAnterior = beneficioInacumulavel.getDibAnterior();

            List<String> datas = gerarDatasPorBeneficioEPeriodo(beneficio,false, inicioDesconto, dataFim);

            BigDecimal rmi = beneficioInacumulavel.getRmi();

            BigDecimal indiceReajuste;

            for (String data : datas) {
                FiltroRecebido filtroRecebido = new FiltroRecebido();

                filtroRecebido.setData(data);
                //Converter dataString para LocalDate
                LocalDate dataCalculo = dateUtils.mapStringToLocalDate(data);

                if (isDataDeReajuste(dataCalculo, inicioDesconto)) {
                    indiceReajuste = retornaIndiceReajuste(dataCalculo, dib, dataDibAnterior);
                    rmi = rmi.multiply(indiceReajuste).setScale(2, RoundingMode.HALF_UP);
                }

                BigDecimal indiceReajusteRecebido = retornaIndiceReajuste(dataCalculo, dib, dataDibAnterior);

                filtroRecebido.setIndiceReajusteRecebido(indiceReajusteRecebido.setScale(4, RoundingMode.HALF_UP));

                BigDecimal recebido = isDecimoTerceiro(data) ? retornaValorDecimoTerceiro(data, inicioDesconto,dataFim, rmi) : calcularValorDevido(dataCalculo,inicioDesconto ,dataFim, rmi);

                filtroRecebido.setRecebido(recebido.setScale(2, RoundingMode.HALF_UP));

                if (dataJaPossuiNoCalculo(data, listaDeCalculo)) {
                    String mesAno = data.substring(2);
                    FiltroRecebido recebidoExistente = listaDeCalculo.stream()
                            .filter(filtro -> filtro.getData().contains(mesAno))
                            .findFirst().orElseThrow();

                    BigDecimal valorRecebido = filtroRecebido.getRecebido();

                    BigDecimal novoValorRecebido = recebidoExistente.getRecebido().add(valorRecebido);

                    recebidoExistente.setRecebido(novoValorRecebido);

                    continue;
                }
                listaDeCalculo.add(filtroRecebido);
            }
        }
        return listaDeCalculo;
    }

    private boolean dataJaPossuiNoCalculo(String data, HashSet<FiltroRecebido> listaDeCalculo) {
        String mesAno = data.substring(2);
        return listaDeCalculo.stream().anyMatch(filtro -> filtro.getData().contains(mesAno));
    }

    private BigDecimal calcularDesconto(String data, HashSet<FiltroRecebido> listaDeCalculo) {
        try {
            return listaDeCalculo.stream()
                    .filter(filtro -> filtro.getData().substring(2).equals(data.substring(2)))
                    .findFirst().orElseThrow().getRecebido();
        } catch (NoSuchElementException e) {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal retornaIndiceReajusteRecebido(String data, HashSet<FiltroRecebido> listaDeCalculo) {
        try {
            return listaDeCalculo.stream()
                    .filter(filtro -> filtro.getData().substring(2).equals(data.substring(2)))
                    .findFirst().orElseThrow().getIndiceReajusteRecebido();
        } catch (NoSuchElementException e) {
            return BigDecimal.ONE;
        }
    }

    private BigDecimal retornaValorDecimoTerceiro(String data, LocalDate inicioCalculo,LocalDate fimCalculo, BigDecimal rmi) {
        return decimoTerceiroService.calcularDecimoTerceiro(data, inicioCalculo,fimCalculo, rmi);
    }

    private BigDecimal retornaSalarioMinimoSeRmiForInferior(BigDecimal rmi, LocalDate dib, BeneficiosEnum beneficio) {
        return rmiService.obterSalarioMinimoOuRmi(rmi, dib, beneficio);
    }

    private BigDecimal calcularValorDevido(LocalDate dataCalculo,LocalDate inicioCalculo, LocalDate fimCalculo, BigDecimal rmi) {
        return rmiService.calcularValorDevido(dataCalculo,inicioCalculo ,fimCalculo, rmi);
    }

    private BigDecimal retornaCalculoJurosPorTipo(LocalDate dataCalculo, CalculadoraRequestDTO infoCalculo) {
        return calculoJurosService.calcularJuros(dataCalculo, infoCalculo);
    }

    private BigDecimal retornaIndiceReajuste(LocalDate dataCalculo, LocalDate dataDib, LocalDate dataDibAnterior) {
        return calculoIndiceReajusteService.calcularIndiceReajusteAnual(dataCalculo, dataDib, dataDibAnterior);
    }

    private BigDecimal calcularCorrecaoMonetariaPorTipo(TipoCorrecaoMonetaria tipoCorrecao, LocalDate dataCalculo, LocalDate atualizarAte) {
        return correcaoMonetariaFactory.getCalculoCorrecaoMonetaria(tipoCorrecao).calcularIndexadorCorrecaoMonetaria(dataCalculo, atualizarAte);
    }


    private List<String> gerarDatasPorBeneficioEPeriodo(BeneficiosEnum beneficio, boolean decimoTerceiroFinalCalculo,LocalDate dataInicio, LocalDate dataFim) {
        return gerarListaDatasService.gerarListaDatasPorBeneficioEperiodo(beneficio,decimoTerceiroFinalCalculo, dataInicio, dataFim);
    }


    private boolean isDecimoTerceiro(String data) {
        return data.split("/")[1].equals(MES_DECIMO_TERCEIRO);
    }

    private boolean isDataDeReajuste(LocalDate dataCalculo, LocalDate inicioCalculo) {
        return dataCalculo.getMonthValue() == 1 && dataCalculo.getYear() >= inicioCalculo.plusYears(1L).getYear();
    }



}
