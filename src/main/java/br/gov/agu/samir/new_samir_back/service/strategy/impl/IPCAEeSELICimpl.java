package br.gov.agu.samir.new_samir_back.service.strategy.impl;

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
    public BigDecimal calcularIndexadorCorrecaoMonetaria(int mes, int ano) {
        LocalDate dataInicioBusca = LocalDate.of(ano, mes, 1);
        LocalDate dataFinalBusca = LocalDate.now().minusMonths(2).withDayOfMonth(1);
        LocalDate dataLimiteSelic = LocalDate.of(2021,11,1);
        BigDecimal valorCorrecao = new BigDecimal("1");

        if(dataInicioBusca.isAfter(dataLimiteSelic)){
            List<SelicModel> listSelic = selicRepository.findAllByDataBetween(dataInicioBusca,dataFinalBusca);
            for (SelicModel selic : listSelic) {
                BigDecimal valorSelic = selic.getValor().divide(new BigDecimal("100"));
                valorCorrecao = valorCorrecao.add(valorSelic);
            }
        }else {
            valorCorrecao = calcularSelicTotal(dataFinalBusca);
            List<IpcaeModel> listIpcae = ipcaeRepository.findAllByDataBetween(dataInicioBusca, LocalDate.of(2021,11,1));
            for (IpcaeModel ipcae : listIpcae) {
                BigDecimal valorIpcae = ipcae.getValor().divide(new BigDecimal("100"));
                valorIpcae = valorIpcae.add(new BigDecimal("1"));
                valorCorrecao = valorCorrecao.multiply(valorIpcae);
            }
        }
        return valorCorrecao;
    }

    private BigDecimal calcularSelicTotal(LocalDate dataAtual){
        BigDecimal valorCorrecao = new BigDecimal("1");
        List<SelicModel> selicList = selicRepository.findAllByDataBetween(LocalDate.of(2021,12,1),dataAtual);
        for (SelicModel selic : selicList) {
            BigDecimal valorSelic = selic.getValor().divide(BigDecimal.valueOf(100));
            valorCorrecao = valorCorrecao.add(valorSelic);
        }
        return valorCorrecao;
    }
}
