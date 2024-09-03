package br.gov.agu.samir.new_samir_back.service.strategy.factory;

import br.gov.agu.samir.new_samir_back.service.strategy.CalculoCorrecaoMonetaria;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class CorrecaoMonetariaFactory {

    private ApplicationContext context;

    public CorrecaoMonetariaFactory(ApplicationContext context) {
        this.context = context;
    }

    public CalculoCorrecaoMonetaria getCalculo(String tipo) {
        if (tipo.equals("INPCeSELIC")) {
            return context.getBean("INPCeSELICimpl", CalculoCorrecaoMonetaria.class);
        } else if (tipo.equals("IPCAEeSELIC")) {
            return context.getBean("IPCAEeSELICimpl", CalculoCorrecaoMonetaria.class);
        } else {
            throw new RuntimeException("Tipo de cálculo não encontrado: " + tipo);
        }
    }
}