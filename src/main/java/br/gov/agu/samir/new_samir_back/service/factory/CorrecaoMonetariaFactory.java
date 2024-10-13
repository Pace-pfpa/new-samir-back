package br.gov.agu.samir.new_samir_back.service.factory;

import br.gov.agu.samir.new_samir_back.enums.TipoCorrecaoMonetaria;
import br.gov.agu.samir.new_samir_back.service.factory.impl.INPCeSELICimpl;
import br.gov.agu.samir.new_samir_back.service.factory.impl.IPCAEeSELICimpl;
import br.gov.agu.samir.new_samir_back.service.factory.interfaces.CalculoCorrecaoMonetaria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CorrecaoMonetariaFactory {

    private final INPCeSELICimpl inpCeSELICimpl;

    private final IPCAEeSELICimpl ipcaEeSELICimpl;

    public CalculoCorrecaoMonetaria getCalculo(TipoCorrecaoMonetaria tipo) {
        return switch (tipo) {
            case TIPO4 -> inpCeSELICimpl;
            case TIPO6 -> ipcaEeSELICimpl;
            default -> throw new IllegalArgumentException("Tipo de correção monetária inválido");
        };
    }
}