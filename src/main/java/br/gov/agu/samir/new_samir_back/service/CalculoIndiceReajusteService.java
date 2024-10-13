package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.request.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.exceptions.ResourceNotFoundException;
import br.gov.agu.samir.new_samir_back.repository.IndiceReajusteRepository;
import br.gov.agu.samir.new_samir_back.util.DateUtils;
import lombok.AllArgsConstructor;
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
     * Ele é bucado no banco 1 ano antes da data de reajuste.
     * Exemplo: DIB = 01/01/2020, o reajuste comum é buscado a partir de 01/01/2019.
     */
    public BigDecimal comumReajuste(String dataReajuste) {
        LocalDate data = dateUtils.mapStringToLocalDate(dataReajuste).minusYears(1);
        return indiceReajusteRepository.findByData(data).orElseThrow(
                ()-> new ResourceNotFoundException("Indice de reajuste não encontrado")
        ).getValor();
    }


    /**
     * @param dib
     * @return BigDecimal
    * O primeiro reajuste é calculado a partir da data DIB.
    */
    public BigDecimal primeiroReajusteSemDibAnterior(LocalDate dib){
        return indiceReajusteRepository.findByData(dib.withDayOfMonth(1)).orElseThrow(
                ()-> new ResourceNotFoundException("Indice de reajuste não encontrado")
        ).getValor();
    }


    /**
     * @return BigDecimal
     * @param dibAnterior, dib
    * Caso as datas de reajuste sejam iguais, o valor do índice é buscado a partir da dibAnterior.
    * Caso contrário, o valor do índice é buscado o primeiro valor a partir da data de reajuste da DIB atual.
    */
    public BigDecimal primeiroReajusteComDibAnterior(LocalDate dibAnterior, LocalDate dib) {
        LocalDate dataReajusteDibAtual = indiceReajusteRepository.findByData(dib.withDayOfMonth(1)).orElseThrow().getDataReajuste();
        LocalDate dataReajusteDibAnterior = indiceReajusteRepository.findByData(dibAnterior.withDayOfMonth(1)).orElseThrow().getDataReajuste();

        if (dataReajusteDibAtual.isEqual(dataReajusteDibAnterior)) {
            return indiceReajusteRepository.findByData(dibAnterior.withDayOfMonth(1)).orElseThrow().getValor();
        }else{
            return indiceReajusteRepository.findFirstByDataReajuste(dataReajusteDibAtual).getValor();
        }
    }
}
