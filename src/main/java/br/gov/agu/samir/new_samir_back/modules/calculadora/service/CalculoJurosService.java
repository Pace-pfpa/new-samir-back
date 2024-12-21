package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.CalculadoraRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.CalculoJurosFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CalculoJurosService {

    private final CalculoJurosFactory calculoJurosFactory;

    public BigDecimal calcularJuros(LocalDate dataCalculo, CalculadoraRequestDTO infoCalculo) {


        if (infoCalculo.getDataIncioJuros().isAfter(LocalDate.of(2021,12,1))){
            return BigDecimal.ZERO;
        }

        if (dataCalculo.isBefore(infoCalculo.getDataIncioJuros())){
            return calculoJurosFactory.getCalculo(infoCalculo.getTipoJuros()).calcularJuros(infoCalculo.getDataIncioJuros(),infoCalculo.getAtualizarAte());
        }
        return calculoJurosFactory.getCalculo(infoCalculo.getTipoJuros()).calcularJuros(dataCalculo,infoCalculo.getAtualizarAte());
    }

}