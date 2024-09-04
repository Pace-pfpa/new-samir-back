package br.gov.agu.samir.new_samir_back.service.strategy.impl;

import br.gov.agu.samir.new_samir_back.models.JurosModel;
import br.gov.agu.samir.new_samir_back.models.SelicModel;
import br.gov.agu.samir.new_samir_back.repository.JurosRepository;
import br.gov.agu.samir.new_samir_back.repository.SelicRepository;
import br.gov.agu.samir.new_samir_back.service.strategy.CalculoJuros;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class JUROSeSELIC implements CalculoJuros {

    private final JurosRepository jurosRepository;

    private final SelicRepository selicRepository;

    public JUROSeSELIC(JurosRepository jurosRepository, SelicRepository selicRepository) {
        this.jurosRepository = jurosRepository;
        this.selicRepository = selicRepository;
    }

    @Override
    public BigDecimal calcularJuros(LocalDate dataAlvo) {
        LocalDate dataFinalBusca = LocalDate.now().minusMonths(2);
        LocalDate dataLimiteSelic = LocalDate.of(2021,11,1);
        BigDecimal valorJuros = BigDecimal.ZERO;

        if(dataAlvo.isAfter(dataLimiteSelic)){
            List<SelicModel> listSelic = selicRepository.findAllByDataBetween(dataAlvo,dataFinalBusca);
            for (SelicModel selicModel : listSelic) {
                valorJuros = valorJuros.add(selicModel.getValor());
            }
            return valorJuros;
        }else{
            valorJuros = calcularSelicTotal();
            List<JurosModel> listJuros = jurosRepository.findAllByDataBetween(dataAlvo,dataLimiteSelic);
            for (JurosModel juros: listJuros) {
                valorJuros = valorJuros.add(juros.getValor());
            }
            return valorJuros;
        }
    }


    private BigDecimal calcularSelicTotal(){
        BigDecimal valorJuros = BigDecimal.ZERO;
        List<SelicModel> listSelic = selicRepository.findAllByDataBetween(LocalDate.of(2021,12,1),LocalDate.now().minusMonths(2));
        for (SelicModel selicModel : listSelic) {
            valorJuros = valorJuros.add(selicModel.getValor());
        }
        return valorJuros;
    }
}
