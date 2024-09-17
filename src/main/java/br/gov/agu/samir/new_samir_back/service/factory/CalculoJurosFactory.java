package br.gov.agu.samir.new_samir_back.service.factory;

import br.gov.agu.samir.new_samir_back.enums.TipoJuros;
import br.gov.agu.samir.new_samir_back.exceptions.ResourceNotFoundException;
import br.gov.agu.samir.new_samir_back.service.factory.impl.JUROSeSELICimpl;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class CalculoJurosFactory {

    private ApplicationContext context;

    public CalculoJurosFactory(ApplicationContext context) {
        this.context = context;
    }

    public CalculoJuros getCalculo(TipoJuros tipo) {
        if (tipo == TipoJuros.TIPO2) {
            return context.getBean(JUROSeSELICimpl.class);
        }
        throw new ResourceNotFoundException("Tipo de cálculo não encontrado");
    }
}
