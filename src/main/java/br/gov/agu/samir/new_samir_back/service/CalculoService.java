package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.CalculoResponseDTO;
import br.gov.agu.samir.new_samir_back.enums.TipoCorrecaoMonetaria;
import br.gov.agu.samir.new_samir_back.service.factory.CalculoJurosFactory;
import br.gov.agu.samir.new_samir_back.service.factory.CorrecaoMonetariaFactory;
import br.gov.agu.samir.new_samir_back.service.strategy.DecimoTerceiroStrategy;
import br.gov.agu.samir.new_samir_back.service.strategy.GerarListaStrategy;
import br.gov.agu.samir.new_samir_back.service.strategy.IndiceReajusteStrategy;
import br.gov.agu.samir.new_samir_back.service.strategy.RmiStrategy;
import br.gov.agu.samir.new_samir_back.service.strategy.impl.CalculoJurosStrategy;
import br.gov.agu.samir.new_samir_back.util.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

@Service
@AllArgsConstructor
public class CalculoService {

    private final List<IndiceReajusteStrategy> indiceReajusteStrategyList;

    private final RmiStrategy rmiStrategy;

    private final GerarListaStrategy gerarListaStrategy;

    private final DecimoTerceiroStrategy decimoTerceiroStrategy;

    private final CorrecaoMonetariaFactory correcaoMonetariaFactory;

    private final CalculoJurosStrategy calculoJurosStrategy;


    public List<CalculoResponseDTO> calculoSemBeneficioAcumulado(CalculoRequestDTO requestDTO) {


       List<String> dataList = gerarListaStrategy.gerarLista(requestDTO);

        List<CalculoResponseDTO> tabela = new ArrayList<>();

        BigDecimal salarioReajustadoAnual = rmiStrategy.calcularRmi(requestDTO, dataList.get(0));

        for (String data : dataList){

            if (isDataDeReajuste(data)){
                salarioReajustadoAnual = rmiStrategy.calcularRmi(requestDTO, data).multiply(indiceReajuste(data, requestDTO)).setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            BigDecimal indiceReajuste = isDecimoTerceiro(data) ? BigDecimal.ONE : indiceReajuste(data, requestDTO);

            BigDecimal devido = salarioReajustadoAnual;

            BigDecimal recebido = BigDecimal.ZERO;

            BigDecimal diferenca = devido.subtract(recebido).setScale(2, BigDecimal.ROUND_HALF_UP);

            TipoCorrecaoMonetaria tipoCorrecao = requestDTO.getTipoCorrecao();

            BigDecimal indiceCorrecaoMonetaria = correcaoMonetariaFactory.getCalculo(tipoCorrecao).calcularIndexadorCorrecaoMonetaria(data);

            BigDecimal salarioCorrigido = devido.multiply(indiceCorrecaoMonetaria).setScale(2, BigDecimal.ROUND_HALF_UP);

            BigDecimal porcentagemJuros = calculoJurosStrategy.calcularJuros(requestDTO, data);

            BigDecimal juros = salarioCorrigido.multiply(porcentagemJuros).setScale(2, BigDecimal.ROUND_HALF_UP);



            if (!isDecimoTerceiro(data)){
                CalculoResponseDTO linha = CalculoResponseDTO.builder()
                        .data(data)
                        .indiceReajusteDevido(indiceReajuste)
                        .devido(devido)
                        .indiceReajusteRecebido(indiceReajuste)
                        .recebido(recebido)
                        .diferenca(diferenca)
                        .indiceCorrecaoMonetaria(indiceCorrecaoMonetaria)
                        .salarioCorrigido(salarioCorrigido)
                        .porcentagemJuros(porcentagemJuros)
                        .juros(juros)
                        .soma(salarioCorrigido)
                        .build();

                tabela.add(linha);
            }else {

                BigDecimal decimoTerceiro = decimoTerceiroStrategy.calcularDecimoTerceiro(requestDTO, devido, data);
                BigDecimal devidoDecimoTerceiro = decimoTerceiro.multiply(indiceReajuste).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal salarioCorrigidoDecimoTerceiro = devidoDecimoTerceiro.multiply(indiceCorrecaoMonetaria).setScale(2, BigDecimal.ROUND_HALF_UP);
                CalculoResponseDTO linha = CalculoResponseDTO.builder()
                        .data(data)
                        .indiceReajusteDevido(indiceReajuste)
                        .devido(devidoDecimoTerceiro)
                        .indiceReajusteRecebido(indiceReajuste)
                        .recebido(BigDecimal.ZERO)
                        .diferenca(devidoDecimoTerceiro)
                        .indiceCorrecaoMonetaria(indiceCorrecaoMonetaria)
                        .salarioCorrigido(salarioCorrigidoDecimoTerceiro)
                        .porcentagemJuros(BigDecimal.ZERO)
                        .juros(BigDecimal.ZERO)
                        .soma(salarioCorrigidoDecimoTerceiro)
                        .build();

                tabela.add(linha);
            }
        }


        return tabela;
    }


    private boolean isDecimoTerceiro(String data){
        return data.split("/")[1].equals("13");
    }

    private boolean isDataDeReajuste(String data){
        return data.split("/")[1].equals("01");
    }

    private BigDecimal indiceReajuste(String data, CalculoRequestDTO requestDTO){
        return indiceReajusteStrategyList.stream().map(impl -> impl.calcularIndiceReajuste(requestDTO, data)).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
