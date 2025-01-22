package br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.impl;

import br.gov.agu.samir.new_samir_back.modules.juros.model.JurosModel;
import br.gov.agu.samir.new_samir_back.modules.selic.model.SelicModel;
import br.gov.agu.samir.new_samir_back.modules.juros.repository.JurosRepository;
import br.gov.agu.samir.new_samir_back.modules.selic.repository.SelicRepository;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.interfaces.CalculoJuros;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class JUROSeSELICimpl implements CalculoJuros {

    private final JurosRepository jurosRepository;

    private final SelicRepository selicRepository;

    private static final LocalDate DATA_LIMITE_SELIC = LocalDate.of(2021,11,1);




    @Override
    public BigDecimal calcularJuros(LocalDate dataCalculo, LocalDate atualizarAte) {

        atualizarAte = atualizarAte.minusMonths(1L);

        return calcularJurosComSelic(dataCalculo, atualizarAte);
    }


    private BigDecimal calcularJurosComSelic(LocalDate dataCalculo, LocalDate atualizarAte){
        BigDecimal valorJuros = retornaSelicTotalAcumulada(atualizarAte);
        List<JurosModel> listJuros = jurosRepository.findAllByDataBetween(dataCalculo.withDayOfMonth(1),DATA_LIMITE_SELIC);
        for (JurosModel juros: listJuros) {
            valorJuros = valorJuros.add(juros.getValor());
        }
        return valorJuros;
    }


    private BigDecimal retornaSelicTotalAcumulada(LocalDate atualizarAte){
        BigDecimal valorJuros = BigDecimal.ZERO;
        List<SelicModel> listSelic = selicRepository.findAllByDataBetween(LocalDate.of(2021,12,1),atualizarAte);
        for (SelicModel selicModel : listSelic) {
            valorJuros = valorJuros.add(selicModel.getValor());
        }
        return valorJuros;
    }
}
