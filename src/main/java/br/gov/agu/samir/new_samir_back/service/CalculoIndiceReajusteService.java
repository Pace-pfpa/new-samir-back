package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.exceptions.ResourceNotFoundException;
import br.gov.agu.samir.new_samir_back.repository.IndiceReajusteRepository;
import br.gov.agu.samir.new_samir_back.util.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;


@Service
@AllArgsConstructor
public class CalculoIndiceReajusteService {

    private final DateUtils dateUtils;

    private final IndiceReajusteRepository indiceReajusteRepository;


    /**
     ** @param dataReajuste
     * @return BigDecimal
     * Reajuste comum é o segundo reajuste em diante, que ocorre no mês de janeiro e a partir do segundo ano da DIB.
     */
    public BigDecimal comumReajuste(String dataReajuste) {
        return indiceReajusteRepository.findFirstByDataReajuste(dateUtils.mapStringToLocalDate(dataReajuste)).getValor();
    }


    /**
     * @param infoCalculo
     * @return BigDecimal
    * O primeiro reajuste é calculado a partir da data DIB.
    */
    public BigDecimal primeiroReajuste(CalculoRequestDTO infoCalculo){
        return indiceReajusteRepository.findByData(infoCalculo.getDib().withDayOfMonth(1)).orElseThrow(
                ()-> new ResourceNotFoundException("Indice de reajuste não encontrado")
        ).getValor();
    }


    /**
     * @return BigDecimal
     * @param infoCalculo
    * Caso as datas de reajuste sejam iguais, o valor do índice é buscado a partir da dibAnterior.
    * Caso contrário, o valor do índice é buscado o primeiro valor a partir da data de reajuste da DIB atual.
    */
    public BigDecimal primeiroReajusteComDibAnterior(CalculoRequestDTO infoCalculo) {
        LocalDate dataReajusteDibAtual = indiceReajusteRepository.findByData(infoCalculo.getDib().withDayOfMonth(1)).orElseThrow().getDataReajuste();
        LocalDate dataReajusteDibAnterior = indiceReajusteRepository.findByData(infoCalculo.getDibAnterior().withDayOfMonth(1)).orElseThrow().getDataReajuste();

        if (dataReajusteDibAtual.isEqual(dataReajusteDibAnterior)) {
            return indiceReajusteRepository.findByData(infoCalculo.getDibAnterior().withDayOfMonth(1)).orElseThrow().getValor();
        }else{
            return indiceReajusteRepository.findFirstByData(dataReajusteDibAtual).getValor();
        }
    }
}
