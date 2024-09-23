package br.gov.agu.samir.new_samir_back.service.factory;

import br.gov.agu.samir.new_samir_back.enums.TipoJuros;
import br.gov.agu.samir.new_samir_back.service.factory.impl.JUROSeSELICimpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.gov.agu.samir.new_samir_back.enums.TipoJuros.TIPO2;

@Service
@RequiredArgsConstructor
public class CalculoJurosFactory {

    private final JUROSeSELICimpl jurosESelic;

    public CalculoJuros getCalculo(TipoJuros tipo) {
        return tipo == TIPO2 ? jurosESelic : null;
    }

}
