package br.gov.agu.samir.new_samir_back.service.factory.impl;

import br.gov.agu.samir.new_samir_back.models.IpcaeModel;
import br.gov.agu.samir.new_samir_back.models.SelicModel;
import br.gov.agu.samir.new_samir_back.repository.IpcaeRepository;
import br.gov.agu.samir.new_samir_back.repository.SelicRepository;
import br.gov.agu.samir.new_samir_back.service.factory.interfaces.CalculoCorrecaoMonetaria;
import lombok.AllArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
public class IPCAEeSELICimpl implements CalculoCorrecaoMonetaria {

    private final SelicRepository selicRepository;

    private final IpcaeRepository ipcaeRepository;

    private final DateTimeFormatter ddMMyyyy;
    
    private static final LocalDate DATA_LIMITE_SELIC = LocalDate.of(2021,11,1);

    @Override
    public BigDecimal calcularIndexadorCorrecaoMonetaria(String data, LocalDate atualizarAte) {
        
        LocalDate dataAlvo = LocalDate.parse(data,ddMMyyyy);
        

        if(dataAlvo.isAfter(DATA_LIMITE_SELIC)){
            return calculoSomenteComSelic(dataAlvo, atualizarAte).setScale(4, RoundingMode.HALF_UP);
        }else {
            return calculoComIPCAEeSELIC(dataAlvo,atualizarAte).setScale(4, RoundingMode.HALF_UP);
        }
    }



    private BigDecimal calculoComIPCAEeSELIC(LocalDate dataAlvo, LocalDate atualizarAte){
        BigDecimal valorCorrecao = calculoSomenteComSelic(LocalDate.of(2021,12,1), atualizarAte);
        List<IpcaeModel> listIPCAE = ipcaeRepository.findAllByDataBetween(dataAlvo, LocalDate.of(2021,11,1));
        for (IpcaeModel inpcModel : listIPCAE) {
            BigDecimal valorIpcae= inpcModel.getValor().divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
            valorIpcae = valorIpcae.add(BigDecimal.valueOf(1));
            valorCorrecao = valorCorrecao.multiply(valorIpcae);
        }
        return valorCorrecao;
    }

    private BigDecimal calculoSomenteComSelic(LocalDate dataAlvo, LocalDate atualizarAte){
        BigDecimal valorCorrecao = BigDecimal.ONE;
        List<SelicModel> listSelic = selicRepository.findAllByDataBetween(dataAlvo,atualizarAte);
        for (SelicModel selic : listSelic) {
            BigDecimal valorSelic = selic.getValor()
                    .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
            valorCorrecao = valorCorrecao.add(valorSelic);
        }
        return valorCorrecao;
    }
}
