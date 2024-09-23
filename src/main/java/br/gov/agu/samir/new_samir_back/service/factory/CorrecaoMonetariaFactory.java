package br.gov.agu.samir.new_samir_back.service.factory;

import br.gov.agu.samir.new_samir_back.enums.TipoCorrecaoMonetaria;
import br.gov.agu.samir.new_samir_back.service.factory.impl.INPCeSELICimpl;
import br.gov.agu.samir.new_samir_back.service.factory.impl.IPCAEeSELICimpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.gov.agu.samir.new_samir_back.enums.TipoCorrecaoMonetaria.TIPO4;

@RequiredArgsConstructor
@Service
public class CorrecaoMonetariaFactory {

    private final INPCeSELICimpl inpCeSELICimpl;

    private final IPCAEeSELICimpl ipcaEeSELICimpl;

    public CalculoCorrecaoMonetaria getCalculo(TipoCorrecaoMonetaria tipo) {
        return tipo == TIPO4 ? inpCeSELICimpl : ipcaEeSELICimpl;
    }
}