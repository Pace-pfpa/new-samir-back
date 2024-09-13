package br.gov.agu.samir.new_samir_back.service.strategy.impl;

import br.gov.agu.samir.new_samir_back.models.InpcModel;
import br.gov.agu.samir.new_samir_back.models.IpcaeModel;
import br.gov.agu.samir.new_samir_back.models.SelicModel;
import br.gov.agu.samir.new_samir_back.repository.IpcaeRepository;
import br.gov.agu.samir.new_samir_back.repository.SelicRepository;
import br.gov.agu.samir.new_samir_back.service.strategy.CalculoCorrecaoMonetaria;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class IPCAEeSELICimpl implements CalculoCorrecaoMonetaria {

    private final SelicRepository selicRepository;

    private final IpcaeRepository ipcaeRepository;

    public IPCAEeSELICimpl(SelicRepository selicRepository, IpcaeRepository ipcaeRepository) {
        this.selicRepository = selicRepository;
        this.ipcaeRepository = ipcaeRepository;
    }

    @Override
    public BigDecimal calcularIndexadorCorrecaoMonetaria(LocalDate dataAlvo) {
        LocalDate dataLimiteSelic = LocalDate.of(2021,11,1);

        if(dataAlvo.isAfter(dataLimiteSelic)){
            return calculoSomenteComSelic(dataAlvo).setScale(4, BigDecimal.ROUND_HALF_UP);
        }else {
            return calculoComIPCAEeSELIC(dataAlvo).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
    }



    private BigDecimal calculoComIPCAEeSELIC(LocalDate dataAlvo){
        BigDecimal valorCorrecao = calculoSomenteComSelic(LocalDate.of(2021,12,1));
        List<IpcaeModel> listIPCAE = ipcaeRepository.findAllByDataBetween(dataAlvo, LocalDate.of(2021,11,1));
        for (IpcaeModel inpcModel : listIPCAE) {
            BigDecimal valorIpcae= inpcModel.getValor().divide(BigDecimal.valueOf(100));
            valorIpcae = valorIpcae.add(BigDecimal.valueOf(1));
            valorCorrecao = valorCorrecao.multiply(valorIpcae);
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
