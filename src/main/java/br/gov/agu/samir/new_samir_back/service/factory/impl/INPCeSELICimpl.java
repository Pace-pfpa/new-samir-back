package br.gov.agu.samir.new_samir_back.service.factory.impl;

import br.gov.agu.samir.new_samir_back.models.InpcModel;
import br.gov.agu.samir.new_samir_back.models.SelicModel;
import br.gov.agu.samir.new_samir_back.repository.InpcRepository;
import br.gov.agu.samir.new_samir_back.repository.SelicRepository;
import br.gov.agu.samir.new_samir_back.service.factory.CalculoCorrecaoMonetaria;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Component
public class INPCeSELICimpl implements CalculoCorrecaoMonetaria {

    private final SelicRepository selicRepository;

    private final InpcRepository inpcRepository;

    private static final LocalDate DATA_LIMITE_SELIC = LocalDate.of(2021,11,1);

    public INPCeSELICimpl(SelicRepository selicRepository, InpcRepository inpcRepository) {
        this.selicRepository = selicRepository;
        this.inpcRepository = inpcRepository;
    }

    @Override
    public BigDecimal calcularIndexadorCorrecaoMonetaria(LocalDate dataAlvo) {

        if(dataAlvo.isAfter(DATA_LIMITE_SELIC)){
            return calculoSomenteComSelic(dataAlvo).setScale(4, BigDecimal.ROUND_HALF_UP);
        }else {
            return calculoComINPCeSELIC(dataAlvo).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
    }


    private BigDecimal calculoComINPCeSELIC(LocalDate dataAlvo){
        BigDecimal valorCorrecao = calculoSomenteComSelic(LocalDate.of(2021,12,1));
        List<InpcModel> listINPC = inpcRepository.findAllByDataBetween(dataAlvo, DATA_LIMITE_SELIC);
        for (InpcModel inpcModel : listINPC) {
            BigDecimal valorInpc = inpcModel.getValor().divide(BigDecimal.valueOf(100));
            valorInpc = valorInpc.add(BigDecimal.valueOf(1));
            valorCorrecao = valorCorrecao.multiply(valorInpc);
        }
        return valorCorrecao;
    }


    private BigDecimal calculoSomenteComSelic(LocalDate dataAlvo){
        BigDecimal valorCorrecao = BigDecimal.ONE;
        List<SelicModel> listSelic = selicRepository.findAllByDataBetween(dataAlvo,LocalDate.now().minusMonths(2));
        for (SelicModel selic : listSelic) {
            BigDecimal valorSelic = selic.getValor()
                    .divide(BigDecimal.valueOf(100));
            valorCorrecao = valorCorrecao.add(valorSelic);
        }
        return valorCorrecao;
    }
}
