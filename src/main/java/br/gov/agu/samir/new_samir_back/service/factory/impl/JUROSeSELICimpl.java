package br.gov.agu.samir.new_samir_back.service.factory.impl;

import br.gov.agu.samir.new_samir_back.models.JurosModel;
import br.gov.agu.samir.new_samir_back.models.SelicModel;
import br.gov.agu.samir.new_samir_back.repository.JurosRepository;
import br.gov.agu.samir.new_samir_back.repository.SelicRepository;
import br.gov.agu.samir.new_samir_back.service.factory.interfaces.CalculoJuros;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class JUROSeSELICimpl implements CalculoJuros {

    private final JurosRepository jurosRepository;

    private final SelicRepository selicRepository;

    private static final LocalDate DATA_LIMITE_SELIC = LocalDate.of(2021,11,1);

    private static final  LocalDate DATA_FINAL_BUSCA = LocalDate.now().minusMonths(2);



    @Override
    public BigDecimal calcularJuros(LocalDate dataCalculo, LocalDate atualizarAte) {

        atualizarAte = atualizarAte.minusMonths(1L);

        return calcularJurosComSelic(dataCalculo, atualizarAte);
    }


    private BigDecimal calcularJurosComSelic(LocalDate dataAlvo, LocalDate atualizarAte){
        BigDecimal valorJuros = retornaSelicTotalAcumulada(atualizarAte);
        List<JurosModel> listJuros = jurosRepository.findAllByDataBetween(dataAlvo,DATA_LIMITE_SELIC);
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
