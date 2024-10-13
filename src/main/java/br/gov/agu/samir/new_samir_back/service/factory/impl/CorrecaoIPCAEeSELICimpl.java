package br.gov.agu.samir.new_samir_back.service.factory.impl;

import br.gov.agu.samir.new_samir_back.models.IpcaeModel;
import br.gov.agu.samir.new_samir_back.models.SelicModel;
import br.gov.agu.samir.new_samir_back.repository.IpcaeRepository;
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
public class CorrecaoIPCAEeSELICimpl implements CalculoCorrecaoMonetaria {

    private final SelicRepository selicRepository;

    private final IpcaeRepository ipcaeRepository;

    private final DateTimeFormatter ddMMyyyy;

    private static final LocalDate DATA_FINAL_IPCAE = LocalDate.of(2021,11,1);

    private static final LocalDate DATA_INICIO_SELIC = LocalDate.of(2021,12,1);

    private static final String MES_DECIMO_TERCEIRO = "13";

    private static final String MES_DEZEMBRO = "12";


    @Override
    public BigDecimal calcularIndexadorCorrecaoMonetaria(String data, LocalDate atualizarAte) {

        if (isDecimoTerceiro(data)){
            data = data.replace(MES_DECIMO_TERCEIRO,MES_DEZEMBRO);
        }

        LocalDate dataFormatada = LocalDate.parse(data,ddMMyyyy).withDayOfMonth(1);
        atualizarAte = atualizarAte.minusMonths(1L);

        if(dataFormatada.isAfter(DATA_FINAL_IPCAE)){
            return retornaSelicAcumulada(dataFormatada, atualizarAte);
        }else {
            return calculoComIPCAEeSELIC(dataFormatada,atualizarAte);
        }
    }


    private BigDecimal calculoComIPCAEeSELIC(LocalDate data, LocalDate atualizarAte){
        BigDecimal totalSelicAcumulada = retornaSelicAcumulada(DATA_INICIO_SELIC, atualizarAte);
        List<IpcaeModel> listIpcaeModel = ipcaeRepository.findAllByDataBetween(data, DATA_FINAL_IPCAE);
        List<BigDecimal> listValoresIpcae = new ArrayList<>();

        for (IpcaeModel ipcaeModel : listIpcaeModel) {
            BigDecimal valorIpcae = ipcaeModel.getValor().divide(BigDecimal.valueOf(100),4, RoundingMode.UNNECESSARY).add(BigDecimal.ONE);
            listValoresIpcae.add(valorIpcae);
        }
        return  listValoresIpcae.stream().reduce(totalSelicAcumulada,BigDecimal::multiply);
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