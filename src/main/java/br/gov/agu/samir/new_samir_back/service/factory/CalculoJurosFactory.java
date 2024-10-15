package br.gov.agu.samir.new_samir_back.service.factory;

import br.gov.agu.samir.new_samir_back.enums.TipoJuros;
import br.gov.agu.samir.new_samir_back.service.factory.impl.JUROSeSELICimpl;
import br.gov.agu.samir.new_samir_back.service.factory.interfaces.CalculoJuros;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class CalculoJurosFactory {

    private final JUROSeSELICimpl jurosESelic;

    public CalculoJuros getCalculo(TipoJuros tipo) {
        if (Objects.requireNonNull(tipo) == TipoJuros.TIPO2) {
            return jurosESelic;
        }
        throw new IllegalArgumentException("Tipo de juros inválido");
    }

}
