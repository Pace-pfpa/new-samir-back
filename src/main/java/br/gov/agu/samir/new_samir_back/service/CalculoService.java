package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.CalculoResponseDTO;
import br.gov.agu.samir.new_samir_back.service.strategy.IndiceReajusteStrategy;
import br.gov.agu.samir.new_samir_back.util.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CalculoService {

    private final List<IndiceReajusteStrategy> indiceReajusteStrategyList;


    public List<CalculoResponseDTO> calculoSemBeneficioAcumulado(CalculoRequestDTO requestDTO) {

        List<String> dataList = DateUtils.gerarListaDataComDecimoTerceiro(requestDTO.getDib(), requestDTO.getDataFim());
        List<CalculoResponseDTO> tabela = new ArrayList<>();

        for (String data : dataList){

            BigDecimal indiceReajusteDevido = indiceReajuste(data, requestDTO);


            if (!isDecimoTerceiro(data)){
                CalculoResponseDTO linha = CalculoResponseDTO.builder()
                        .data(data)
                        .indiceReajusteDevido(indiceReajusteDevido)
                        .devido()

            }
        }


        return null;
    }


    private boolean isDecimoTerceiro(String data){
        return data.split("/")[1].equals("13");
    }

    private BigDecimal indiceReajuste(String data, CalculoRequestDTO requestDTO){
        return indiceReajusteStrategyList.stream().map(impl -> impl.calcularIndiceReajuste(requestDTO, data)).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
