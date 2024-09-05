package br.gov.agu.samir.new_samir_back.service.strategy.impl;

import br.gov.agu.samir.new_samir_back.models.InpcModel;
import br.gov.agu.samir.new_samir_back.models.SelicModel;
import br.gov.agu.samir.new_samir_back.repository.InpcRepository;
import br.gov.agu.samir.new_samir_back.repository.SelicRepository;
import br.gov.agu.samir.new_samir_back.service.strategy.CalculoCorrecaoMonetaria;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Component
public class INPCeSELICimpl implements CalculoCorrecaoMonetaria {

    private final SelicRepository selicRepository;

    private final InpcRepository inpcRepository;

    public INPCeSELICimpl(SelicRepository selicRepository, InpcRepository inpcRepository) {
        this.selicRepository = selicRepository;
        this.inpcRepository = inpcRepository;
    }

    @Override
    public BigDecimal calcularIndexadorCorrecaoMonetaria(LocalDate dataAlvo) {
        LocalDate dataFinalBusca = LocalDate.now().minusMonths(2).withDayOfMonth(1);
        LocalDate dataLimiteSelic = LocalDate.of(2021,11,1);
        BigDecimal valorCorrecao = BigDecimal.ONE;

        if(dataAlvo.isAfter(dataLimiteSelic)){
            List<SelicModel> listSelic = selicRepository.findAllByDataBetween(dataAlvo,dataFinalBusca);
            for (SelicModel selic : listSelic) {
                BigDecimal valorSelic = selic.getValor().divide(new BigDecimal("100"));
                valorCorrecao = valorCorrecao.add(valorSelic);
            }
        }else {
            valorCorrecao = calcularSelicTotal();
            List<InpcModel> listInpc = inpcRepository.findAllByDataBetween(dataAlvo, LocalDate.of(2021,11,1));
            for (InpcModel inpc : listInpc) {
                BigDecimal valorInpc = inpc.getValor().divide(new BigDecimal("100"));
                valorInpc = valorInpc.add(BigDecimal.ONE);
                valorCorrecao = valorCorrecao.multiply(valorInpc);
            }
        }
        return valorCorrecao;
    }

    //isso ta funcionando
    private BigDecimal calcularSelicTotal(){
        BigDecimal valorCorrecao = BigDecimal.ONE;
        List<SelicModel> selicList = selicRepository.findAllByDataBetween(LocalDate.of(2021,12,1),LocalDate.now().minusMonths(2));
        for (SelicModel selic : selicList) {
            BigDecimal valorSelic = selic.getValor().divide(BigDecimal.valueOf(100));
            valorCorrecao = valorCorrecao.add(valorSelic);
        }
        return valorCorrecao;
    }
}
