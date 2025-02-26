package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.beneficio.dto.BeneficioAcumuladoRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.beneficio.enums.BeneficiosEnum;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.CalculadoraRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.LinhaTabelaDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo.CompetenciaDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo.DevidoRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo.RecebidoRequestDTO;
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
public class CompetenciaService {

    private final RmiService rmiService;
    private final DecimoTerceiroService decimoTerceiroService;
    private final CalculoJurosService calculoJurosService;
    private final CalculoIndiceReajusteService calculoIndiceReajusteService;
    private final GerarListaDatasService gerarListaDatasService;

    private final CorrecaoMonetariaFactory correcaoMonetariaFactory;

    private final DateUtils dateUtils;

    private static final String MES_DECIMO_TERCEIRO = "13";


    public List<CompetenciaDTO> gerarCompetencias(CalculoRequestDTO requestDTO, DevidoRequestDTO calculoDevido) {
        BeneficiosEnum beneficioVigente = BeneficiosEnum.getByNome(calculoDevido.getEspecie());

        LocalDate dib = calculoDevido.getDib();

        LocalDate inicioCalculo = calculoDevido.getDataInicial();

        LocalDate fimCalculo = calculoDevido.getDataFinal();

        LocalDate dibAnterior = calculoDevido.getNbAnterior();

        LocalDate atualizarAte = requestDTO.getCalculadoPara();

        BigDecimal rmi = rmiService.reajustarRmi(calculoDevido);

        rmi = retornaSalarioMinimoSeRmiForInferior(rmi, dib, beneficioVigente);

        TipoCorrecaoMonetaria tipoCorrecao = TipoCorrecaoMonetaria.getTipoCorrecaoMonetaria(requestDTO.getTipoCorrecao());

        boolean decimoTerceiroFinalCalculo = calculoDevido.isDecimoTerceiroFimCalculo();

        List<CompetenciaDTO> competencias = new ArrayList<>();

        List<String> listaDeDatasParaCalculo = gerarDatasPorBeneficioEPeriodo(beneficioVigente,decimoTerceiroFinalCalculo, inicioCalculo, fimCalculo);

        BigDecimal indiceReajuste;

        HashSet<FiltroRecebido> listaDeCalculoRecebido = gerarListaDeCalculoParaRecebido(calculoDevido.getRecebidos());

        for (String data : listaDeDatasParaCalculo) {
            CompetenciaDTO competencia = new CompetenciaDTO();
            competencia.setData(data);
            //Converter a data para LocalDate
            LocalDate dataCalculo = dateUtils.mapStringToLocalDate(data);

            if (isDataDeReajuste(dataCalculo, inicioCalculo)) {
                indiceReajuste = retornaIndiceReajuste(dataCalculo, dib, dibAnterior);
                rmi = rmi.multiply(indiceReajuste).setScale(2, RoundingMode.HALF_UP);
            }

            BigDecimal indiceReajusteDevido = retornaIndiceReajuste(dataCalculo, dib, dibAnterior);
            competencia.setIndiceReajusteDevido(indiceReajusteDevido.setScale(4, RoundingMode.HALF_UP));

            BigDecimal valorDevido = isDecimoTerceiro(data) ? retornaValorDecimoTerceiro(data, inicioCalculo,fimCalculo, rmi) : calcularValorDevido(dataCalculo,inicioCalculo,fimCalculo, rmi);
            competencia.setDevido(valorDevido.setScale(2, RoundingMode.HALF_UP));

            BigDecimal indiceReajusteRecebido = retornaIndiceReajusteRecebido(data, listaDeCalculoRecebido);
            competencia.setIndiceReajusteRecebido(indiceReajusteRecebido.setScale(4, RoundingMode.HALF_UP));

            BigDecimal recebido = calcularDesconto(data, listaDeCalculoRecebido);
            competencia.setRecebido(recebido.setScale(2, RoundingMode.HALF_UP));

            BigDecimal diferenca = competencia.getDevido().subtract(competencia.getRecebido());
            competencia.setDiferenca(diferenca.setScale(2, RoundingMode.HALF_UP));

            BigDecimal correcaoMonetaria = calcularCorrecaoMonetariaPorTipo(tipoCorrecao, dataCalculo, atualizarAte);
            competencia.setIndiceCorrecaoMonetaria(correcaoMonetaria.setScale(4, RoundingMode.HALF_UP));

            BigDecimal salarioCorrigido = competencia.getDiferenca().multiply(competencia.getIndiceCorrecaoMonetaria());
            competencia.setSalarioCorrigido(salarioCorrigido.setScale(2, RoundingMode.HALF_UP));

            BigDecimal jurosPorcentagem = retornaCalculoJurosPorTipo(dataCalculo, requestDTO);
            competencia.setPorcentagemJuros(jurosPorcentagem.setScale(4, RoundingMode.HALF_UP));

            BigDecimal juros = jurosPorcentagem.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
            juros = juros.multiply(competencia.getSalarioCorrigido());
            competencia.setJuros(juros.setScale(2, RoundingMode.HALF_UP));

            BigDecimal soma = competencia.getSalarioCorrigido().add(competencia.getJuros());
            competencia.setSoma(soma.setScale(2, RoundingMode.HALF_UP));

            competencias.add(competencia);
        }
        return competencias;
    }


    //TODO: Refatorar Calculo do recebido
    private HashSet<FiltroRecebido> gerarListaDeCalculoParaRecebido(List<RecebidoRequestDTO> recebidos) {
        HashSet<FiltroRecebido> listaDeCalculo = new HashSet<>();

        for (RecebidoRequestDTO calculorecebido : recebidos) {

            BeneficiosEnum beneficio = BeneficiosEnum.getByNome(calculorecebido.getEspecie());

            LocalDate dib = calculorecebido.getDib();

            LocalDate inicioDesconto = calculorecebido.getDataInicial();

            LocalDate dataFim = calculorecebido.getDataFinal();

            LocalDate dataDibAnterior = calculorecebido.getNbAnterior();

            List<String> datas = gerarDatasPorBeneficioEPeriodo(beneficio,false, inicioDesconto, dataFim);

            BigDecimal rmi = calculorecebido.getRmi();

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

    private BigDecimal retornaCalculoJurosPorTipo(LocalDate dataCalculo, CalculoRequestDTO calculoRequest) {
        return calculoJurosService.calcularJuros(dataCalculo, calculoRequest);
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
