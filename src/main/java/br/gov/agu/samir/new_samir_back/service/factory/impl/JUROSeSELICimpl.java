package br.gov.agu.samir.new_samir_back.service.factory.impl;

import br.gov.agu.samir.new_samir_back.models.JurosModel;
import br.gov.agu.samir.new_samir_back.models.SelicModel;
import br.gov.agu.samir.new_samir_back.repository.JurosRepository;
import br.gov.agu.samir.new_samir_back.repository.SelicRepository;
import br.gov.agu.samir.new_samir_back.service.factory.CalculoJuros;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class JUROSeSELICimpl implements CalculoJuros {

    private final JurosRepository jurosRepository;

    private final SelicRepository selicRepository;

    private final static LocalDate DATA_LIMITE_SELIC = LocalDate.of(2021,11,1);

    private final static  LocalDate DATA_FINAL_BUSCA = LocalDate.now().minusMonths(2);

    public JUROSeSELICimpl(JurosRepository jurosRepository, SelicRepository selicRepository) {
        this.jurosRepository = jurosRepository;
        this.selicRepository = selicRepository;
    }

    @Override
    public BigDecimal calcularJuros(LocalDate dataAlvo) {
        if(dataAlvo.isAfter(DATA_LIMITE_SELIC)){
            return calculoSomenteComSelic(dataAlvo).setScale(4, BigDecimal.ROUND_HALF_UP);
        }else{
            return calculoComJurosESelic(dataAlvo).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
    }


    private BigDecimal calculoComJurosESelic(LocalDate dataAlvo){
        BigDecimal valorJuros = retornaSelicTotalSelicTotal();
        List<JurosModel> listJuros = jurosRepository.findAllByDataBetween(dataAlvo,DATA_LIMITE_SELIC);
        for (JurosModel juros: listJuros) {
            valorJuros = valorJuros.add(juros.getValor());
        }
        return valorJuros;
    }

    private BigDecimal calculoSomenteComSelic(LocalDate dataAlvo){
        BigDecimal valorJuros = BigDecimal.ZERO;
        List<SelicModel> listSelic = selicRepository.findAllByDataBetween(dataAlvo,DATA_FINAL_BUSCA);
        for (SelicModel selicModel : listSelic) {
            valorJuros = valorJuros.add(selicModel.getValor());
        }
        return valorJuros;

    }

    private BigDecimal retornaSelicTotalSelicTotal(){
        BigDecimal valorJuros = BigDecimal.ZERO;
        List<SelicModel> listSelic = selicRepository.findAllByDataBetween(LocalDate.of(2021,12,1),LocalDate.now().minusMonths(2));
        for (SelicModel selicModel : listSelic) {
            valorJuros = valorJuros.add(selicModel.getValor());
        }
        return valorJuros;
    }
}