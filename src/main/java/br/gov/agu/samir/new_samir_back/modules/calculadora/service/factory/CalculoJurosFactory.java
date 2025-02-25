package br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory;

import br.gov.agu.samir.new_samir_back.modules.calculadora.enums.TipoJuros;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.impl.JUROSeSELICimpl;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.interfaces.CalculoJuros;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class CalculoJurosFactory {

    private final JUROSeSELICimpl jurosESelic;

    public CalculoJuros getCalculo(TipoJuros tipo) {
        if (Objects.requireNonNull(tipo) == TipoJuros.TIPO1) {
            return jurosESelic;
        }
        throw new IllegalArgumentException("Tipo de juros inválido");
    }

}
