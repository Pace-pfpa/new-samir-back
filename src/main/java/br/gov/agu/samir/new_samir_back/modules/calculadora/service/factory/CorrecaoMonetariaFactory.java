package br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory;

import br.gov.agu.samir.new_samir_back.modules.calculadora.enums.TipoCorrecaoMonetaria;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.impl.CorrecaoINPCeSELICimpl;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.impl.CorrecaoIPCAEeSELICimpl;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.interfaces.CalculoCorrecaoMonetaria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CorrecaoMonetariaFactory {

    private final CorrecaoINPCeSELICimpl correcaoInpCeSELICimpl;

    private final CorrecaoIPCAEeSELICimpl correcaoIpcaEeSELICimpl;

    public CalculoCorrecaoMonetaria getCalculo(TipoCorrecaoMonetaria tipo) {
        return switch (tipo) {
            case TIPO4 -> correcaoInpCeSELICimpl;
            case TIPO6 -> correcaoIpcaEeSELICimpl;
            default -> throw new IllegalArgumentException("Tipo de correção monetária inválido");
        };
    }
}