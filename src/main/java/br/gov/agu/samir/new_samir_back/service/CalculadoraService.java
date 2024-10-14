package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.request.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.response.CalculoResponseDTO;
import br.gov.agu.samir.new_samir_back.enums.BeneficiosEnum;
import br.gov.agu.samir.new_samir_back.enums.TipoCorrecaoMonetaria;
import br.gov.agu.samir.new_samir_back.service.factory.CorrecaoMonetariaFactory;
import br.gov.agu.samir.new_samir_back.util.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CalculadoraService {

    private final GerarListaDatasService gerarListaDatasService;

    private final CorrecaoMonetariaFactory correcaoMonetariaFactory;

    private final CalculoIndiceReajusteService calculoIndiceReajusteService;

    private final CalculoJurosService calculoJurosService;
    
    private final DecimoTerceiroService decimoTerceiroService;

    private final SalarioMinimoService salarioMinimoService;

    private final DateUtils dateUtils;

    private static final String MES_DECIMO_TERCEIRO = "13";




    public List<CalculoResponseDTO> calculoSemBeneficioAcumulado(CalculoRequestDTO infoCalculo) {
        List<CalculoResponseDTO> tabelaCalculo = new ArrayList<>();
        List<String> listaDeDatasParaCalculo = gerarDatasPorBeneficioEPeriodo(infoCalculo.getBeneficio(), infoCalculo.getDib(), infoCalculo.getDataFim());
        BigDecimal indiceReajuste = BigDecimal.ONE;
        BigDecimal salarioMinimo = retornaSalarioMaisMinimoProximoPorDataNoMesmoAno(infoCalculo.getDib());
        BigDecimal rmi = retornarSalarioMinimoSeRmiForInferior(infoCalculo.getRmi(), salarioMinimo);

        for (String data : listaDeDatasParaCalculo){
            CalculoResponseDTO linhaTabela = new CalculoResponseDTO();
            linhaTabela.setData(data);
            //Converter a data para LocalDate
            LocalDate dataCalculo = dateUtils.mapStringToLocalDate(data);

            if (isDataDeReajuste(dataCalculo, infoCalculo.getDib())){
                indiceReajuste = retornaIndiceReajuste(dataCalculo, infoCalculo.getDib(), infoCalculo.getDibAnterior());
                rmi = rmi.multiply(indiceReajuste).setScale(2, RoundingMode.HALF_UP);
            }
            linhaTabela.setIndiceReajusteDevido(indiceReajuste);
            BigDecimal devido = isDecimoTerceiro(data) ? retornaValorDecimoTerceiro(data,infoCalculo.getDib(), rmi): calcularRmiPorDiasTrabalhadosNoMes(dataCalculo, rmi);
            linhaTabela.setDevido(devido);
            linhaTabela.setIndiceReajusteRecebido(BigDecimal.ONE);
            linhaTabela.setRecebido(BigDecimal.ZERO);
            linhaTabela.setDiferenca(devido);
            linhaTabela.setIndiceCorrecaoMonetaria(calcularCorrecaoMonetariaPorTipo(infoCalculo.getTipoCorrecao(),dataCalculo,infoCalculo.getAtualizarAte()));
            linhaTabela.setSalarioCorrigido(linhaTabela.getDiferenca().multiply(linhaTabela.getIndiceCorrecaoMonetaria()));
            BigDecimal jurosPorcentagem = retornaCalculoJurosPorTipo(dataCalculo, infoCalculo);
            linhaTabela.setPorcentagemJuros(jurosPorcentagem);

            //TODO
            // AQUI TA QUEBRADO MEU FILHO
            BigDecimal juros = jurosPorcentagem.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
            juros = juros.multiply(linhaTabela.getSalarioCorrigido());
            linhaTabela.setJuros(juros);
            linhaTabela.setSoma(linhaTabela.getSalarioCorrigido().add(linhaTabela.getJuros()));

            tabelaCalculo.add(linhaTabela);
        }
        return tabelaCalculo;
    }


    private BigDecimal retornaValorDecimoTerceiro(String data, LocalDate dib, BigDecimal rmi){
        return decimoTerceiroService.calcularDecimoTerceiro(data,dib,rmi);
    }



    //TODO
    // ISSO AQUI PRECISA MEXER
    private BigDecimal calcularRmiPorDiasTrabalhadosNoMes(LocalDate dataCalculo, BigDecimal rmi){
        int diasTrabalhados = 31 - dataCalculo.getDayOfMonth();
        return rmi.divide(BigDecimal.valueOf(30), RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(diasTrabalhados));
    }


    private BigDecimal retornaCalculoJurosPorTipo(LocalDate dataCalculo, CalculoRequestDTO infoCalculo){
        return calculoJurosService.calcularJuros(dataCalculo, infoCalculo);
    }

    private BigDecimal retornaIndiceReajuste(LocalDate dataCalculo, LocalDate dataDib, LocalDate dataDibAnterior){
        return calculoIndiceReajusteService.calcularIndiceReajusteAnual(dataCalculo, dataDib, dataDibAnterior);
    }

    private BigDecimal calcularCorrecaoMonetariaPorTipo(TipoCorrecaoMonetaria tipoCorrecao, LocalDate dataCalculo, LocalDate atualizarAte){
        return correcaoMonetariaFactory.getCalculo(tipoCorrecao).calcularIndexadorCorrecaoMonetaria(dataCalculo, atualizarAte);
    }

    private  BigDecimal retornarSalarioMinimoSeRmiForInferior(BigDecimal rmi, BigDecimal salarioMinimo){
        return isRmiMenorSalarioMinimo(rmi, salarioMinimo) ? salarioMinimo : rmi;
    }

    private BigDecimal retornaSalarioMaisMinimoProximoPorDataNoMesmoAno(LocalDate data){
        return salarioMinimoService.getSalarioMinimoProximoPorDataNoMesmoAno(data);
    }

    private List<String> gerarDatasPorBeneficioEPeriodo(BeneficiosEnum beneficio, LocalDate dataInicio, LocalDate dataFim){
        return gerarListaDatasService.gerarListaDatasPorBeneficioEperiodo(beneficio, dataInicio, dataFim);
    }

    private boolean isRmiMenorSalarioMinimo(BigDecimal rmi, BigDecimal salarioMinimo){
        return rmi.compareTo(salarioMinimo) < 0;
    }


    private boolean isDecimoTerceiro(String data){
        return data.split("/")[1].equals(MES_DECIMO_TERCEIRO);
    }

    private boolean isDataDeReajuste(LocalDate dataCalculo, LocalDate dataDib){
        return dataCalculo.getMonthValue() == 1 && dataCalculo.getYear() == dataDib.plusYears(1L).getYear();
    }

}
