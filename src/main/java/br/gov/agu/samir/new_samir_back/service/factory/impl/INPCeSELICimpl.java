package br.gov.agu.samir.new_samir_back.service.factory.impl;

import br.gov.agu.samir.new_samir_back.models.InpcModel;
import br.gov.agu.samir.new_samir_back.models.SelicModel;
import br.gov.agu.samir.new_samir_back.repository.InpcRepository;
import br.gov.agu.samir.new_samir_back.repository.SelicRepository;
import br.gov.agu.samir.new_samir_back.service.factory.interfaces.CalculoCorrecaoMonetaria;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
@AllArgsConstructor
public class INPCeSELICimpl implements CalculoCorrecaoMonetaria {

    private final SelicRepository selicRepository;

    private final InpcRepository inpcRepository;

    private final DateTimeFormatter ddMMyyyy;

    private static final LocalDate DATA_LIMITE_SELIC = LocalDate.of(2021,11,1);

    private static final String MES_DECIMO_TERCEIRO = "13";

    private static final String MES_DEZEMBRO = "12";


    @Override
    public BigDecimal calcularIndexadorCorrecaoMonetaria(String data, LocalDate atualizarAte) {

        if (isDecimoTerceiro(data)){
            data = data.replace(MES_DECIMO_TERCEIRO,MES_DEZEMBRO);
        }

        LocalDate dataAlvo = LocalDate.parse(data,ddMMyyyy);

        if(dataAlvo.isAfter(DATA_LIMITE_SELIC)){
            return calculoSomenteComSelic(dataAlvo.withDayOfMonth(1), atualizarAte);
        }else {
            return calculoComINPCeSELIC(dataAlvo.withDayOfMonth(1), atualizarAte);
        }
    }


    private BigDecimal calculoComINPCeSELIC(LocalDate dataAlvo, LocalDate atualizarAte){
        BigDecimal valorCorrecao = calculoSomenteComSelic(LocalDate.of(2021,12,1), atualizarAte.minusMonths(1L));
        List<InpcModel> listINPC = inpcRepository.findAllByDataBetween(dataAlvo, DATA_LIMITE_SELIC);
        for (InpcModel inpcModel : listINPC) {
            BigDecimal valorInpc = inpcModel.getValor().divide(BigDecimal.valueOf(100),4, RoundingMode.UNNECESSARY);
            valorInpc = valorInpc.add(BigDecimal.valueOf(1));
            valorCorrecao = valorCorrecao.multiply(valorInpc);
        }
        return valorCorrecao;
    }


    private BigDecimal calculoSomenteComSelic(LocalDate dataAlvo, LocalDate atualizarAte){
        BigDecimal valorCorrecao = BigDecimal.ONE;
        List<SelicModel> listSelic = selicRepository.findAllByDataBetween(dataAlvo, atualizarAte.minusMonths(1L));
        for (SelicModel selic : listSelic) {
            BigDecimal valorSelic = selic.getValor().divide(BigDecimal.valueOf(100),4, RoundingMode.UNNECESSARY);
            valorCorrecao = valorCorrecao.add(valorSelic);
        }
        return valorCorrecao;
    }

    private boolean isDecimoTerceiro(String data){
        return data.split("/")[1].equals(MES_DECIMO_TERCEIRO);
    }
}
