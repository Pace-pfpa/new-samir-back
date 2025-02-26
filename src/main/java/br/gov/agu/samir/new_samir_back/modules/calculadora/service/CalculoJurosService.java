package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.CalculadoraRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.enums.TipoJuros;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.CalculoJurosFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CalculoJurosService {

    private final CalculoJurosFactory calculoJurosFactory;

    public BigDecimal calcularJuros(LocalDate dataCalculo, CalculoRequestDTO requestDTO) {
        TipoJuros tipoJuros = TipoJuros.getByTipo(requestDTO.getTipoJuros());

        if (requestDTO.getCitacao().isAfter(LocalDate.of(2021,12,1))){
            return BigDecimal.ZERO;
        }
        if (dataCalculo.isBefore(requestDTO.getCitacao() )){
            return calculoJurosFactory.getCalculo(tipoJuros).calcularJuros(requestDTO.getCitacao(),requestDTO.getCalculadoPara());
        }
        return calculoJurosFactory.getCalculo(tipoJuros).calcularJuros(dataCalculo,requestDTO.getCalculadoPara());
    }

}
