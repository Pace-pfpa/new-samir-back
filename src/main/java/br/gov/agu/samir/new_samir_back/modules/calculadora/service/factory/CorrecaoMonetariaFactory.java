package br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory;

import br.gov.agu.samir.new_samir_back.modules.calculadora.enums.TipoCorrecaoMonetaria;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.impl.CorrecaoINPCeSELICimpl;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.impl.CorrecaoIPCAEeSELICimpl;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.impl.SemCorrecaoImpl;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.interfaces.CalculoCorrecaoMonetaria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CorrecaoMonetariaFactory {

    private final CorrecaoINPCeSELICimpl correcaoInpCeSELICimpl;
    private final CorrecaoIPCAEeSELICimpl correcaoIpcaEeSELICimpl;
    private final SemCorrecaoImpl semCorrecaoImpl;


    public CalculoCorrecaoMonetaria getCalculoCorrecaoMonetaria(TipoCorrecaoMonetaria tipo) {
        return switch (tipo) {
            case TIPO1 -> semCorrecaoImpl;
            case TIPO2 -> correcaoInpCeSELICimpl;
            case TIPO3 -> correcaoIpcaEeSELICimpl;
            default -> throw new IllegalArgumentException("Tipo de correção monetária inválido");
        };
    }
}