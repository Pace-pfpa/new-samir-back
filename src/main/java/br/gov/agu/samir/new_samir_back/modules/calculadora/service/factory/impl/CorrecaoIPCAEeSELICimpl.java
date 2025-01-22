package br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.impl;

import br.gov.agu.samir.new_samir_back.modules.ipcae.model.IpcaeModel;
import br.gov.agu.samir.new_samir_back.modules.selic.model.SelicModel;
import br.gov.agu.samir.new_samir_back.modules.ipcae.repository.IpcaeRepository;
import br.gov.agu.samir.new_samir_back.modules.selic.repository.SelicRepository;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.factory.interfaces.CalculoCorrecaoMonetaria;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CorrecaoIPCAEeSELICimpl implements CalculoCorrecaoMonetaria {

    private final SelicRepository selicRepository;

    private final IpcaeRepository ipcaeRepository;

    private static final LocalDate DATA_FINAL_IPCAE = LocalDate.of(2021,11,1);

    private static final LocalDate DATA_INICIO_SELIC = LocalDate.of(2021,12,1);



    @Override
    public BigDecimal calcularIndexadorCorrecaoMonetaria(LocalDate dataCalculo, LocalDate atualizarAte) {

        atualizarAte = atualizarAte.minusMonths(1L);

        if(dataCalculo.isAfter(DATA_FINAL_IPCAE)){
            return retornaSelicAcumulada(dataCalculo, atualizarAte);
        }else {
            return calculoComIPCAEeSELIC(dataCalculo,atualizarAte);
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
}
