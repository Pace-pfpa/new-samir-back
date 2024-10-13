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
import java.util.ArrayList;
import java.util.List;


@Component
@AllArgsConstructor
public class CorrecaoINPCeSELICimpl implements CalculoCorrecaoMonetaria {

    private final SelicRepository selicRepository;

    private final InpcRepository inpcRepository;

    private final DateTimeFormatter ddMMyyyy;

    private static final LocalDate DATA_FINAL_INPC = LocalDate.of(2021,11,1);

    private static final LocalDate INICIO_SELIC = LocalDate.of(2021,12,1);

    private static final String MES_DECIMO_TERCEIRO = "13";

    private static final String MES_DEZEMBRO = "12";




    @Override
    public BigDecimal calcularIndexadorCorrecaoMonetaria(String data, LocalDate atualizarAte) {

        atualizarAte = atualizarAte.minusMonths(1L);


        if (isDecimoTerceiro(data)){
            data = data.replace(MES_DECIMO_TERCEIRO,MES_DEZEMBRO);
        }

        LocalDate dataFormatada = LocalDate.parse(data,ddMMyyyy);

        if(dataFormatada.isAfter(DATA_FINAL_INPC)){
            return retornaSelicAcumulada(dataFormatada.withDayOfMonth(1), atualizarAte);
        }else {
            return calculoComINPCeSELIC(dataFormatada.withDayOfMonth(1), atualizarAte);
        }
    }


    private BigDecimal calculoComINPCeSELIC(LocalDate data, LocalDate atualizarAte){
        BigDecimal totalSelicAcumulada = retornaSelicAcumulada(INICIO_SELIC, atualizarAte);
        List<InpcModel> listINPC = inpcRepository.findAllByDataBetween(data, DATA_FINAL_INPC);
        List<BigDecimal> listValoresInpc = new ArrayList<>();
        for (InpcModel inpcModel : listINPC) {
            BigDecimal valorInpc = inpcModel.getValor().divide(BigDecimal.valueOf(100),4, RoundingMode.UNNECESSARY).add(BigDecimal.ONE);
            listValoresInpc.add(valorInpc);
        }
        return  listValoresInpc.stream().reduce(totalSelicAcumulada,BigDecimal::multiply);
    }

    private BigDecimal retornaSelicAcumulada(LocalDate data, LocalDate atualizarAte){
        List<BigDecimal> listSelic = new ArrayList<>();
        List<SelicModel> listSelicModel = selicRepository.findAllByDataBetween(data, atualizarAte);
        for (SelicModel selic : listSelicModel) {
            BigDecimal valorSelic = selic.getValor().divide(BigDecimal.valueOf(100),4, RoundingMode.UNNECESSARY);
            listSelic.add(valorSelic);
        }
        return listSelic.stream().reduce(BigDecimal.ONE,BigDecimal::add);
    }

    private boolean isDecimoTerceiro(String data){
        return data.split("/")[1].equals(MES_DECIMO_TERCEIRO);
    }
}