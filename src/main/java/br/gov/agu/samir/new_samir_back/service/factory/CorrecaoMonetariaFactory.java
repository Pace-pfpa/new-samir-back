package br.gov.agu.samir.new_samir_back.service.factory;

import br.gov.agu.samir.new_samir_back.enums.TipoCorrecaoMonetaria;
import br.gov.agu.samir.new_samir_back.exceptions.ResourceNotFoundException;
import br.gov.agu.samir.new_samir_back.service.factory.impl.INPCeSELICimpl;
import br.gov.agu.samir.new_samir_back.service.factory.impl.IPCAEeSELICimpl;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class CorrecaoMonetariaFactory {

    private ApplicationContext context;

    public CorrecaoMonetariaFactory(ApplicationContext context) {
        this.context = context;
    }

    public CalculoCorrecaoMonetaria getCalculo(TipoCorrecaoMonetaria tipo) {
        if (tipo == TipoCorrecaoMonetaria.TIPO4) {
            return context.getBean(INPCeSELICimpl.class);
        }
        if (tipo == TipoCorrecaoMonetaria.TIPO6) {
            return context.getBean(IPCAEeSELICimpl.class);
        }
        throw new ResourceNotFoundException("Tipo de cálculo não encontrado");
    }
}