package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.service.factory.CalculoJurosFactory;
import br.gov.agu.samir.new_samir_back.util.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CalculoJurosService {


    private final DateUtils dateUtils;

    private final CalculoJurosFactory calculoJurosFactory;

    public BigDecimal calcularJuros(CalculoRequestDTO infoCalulo, String data) {


        LocalDate dataFormatada = isDecimoTerceiro(data) ? dateUtils.mapDecimoTerceiroToLocalDate(data) : dateUtils.mapStringToLocalDate(data);

        return  isDataAntesDaCitacao(infoCalulo.getDataIncioJuros(), dataFormatada) ?
                calculoJurosFactory.getCalculo(infoCalulo.getTipoJuros()).calcularJuros(infoCalulo.getDataIncioJuros()) :
                calculoJurosFactory.getCalculo(infoCalulo.getTipoJuros()).calcularJuros(dataFormatada);
    }


    private boolean isDataAntesDaCitacao(LocalDate dataCitacao, LocalDate data){
        return  data.isBefore(dataCitacao);
    }

    private boolean isDecimoTerceiro(String data) {
        return data.split("/")[1].equals("13");
    }
}
