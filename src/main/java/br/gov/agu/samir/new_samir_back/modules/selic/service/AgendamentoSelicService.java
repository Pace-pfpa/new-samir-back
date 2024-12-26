package br.gov.agu.samir.new_samir_back.modules.selic.service;

import br.gov.agu.samir.new_samir_back.modules.selic.dto.SelicRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.selic.mapper.SelicMapper;
import br.gov.agu.samir.new_samir_back.modules.selic.model.SelicModel;
import br.gov.agu.samir.new_samir_back.modules.selic.repository.SelicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AgendamentoSelicService {

    private final RestTemplate restTemplate;
    private final SelicRepository selicRepository;
    private final SelicMapper selicMapper;

    public AgendamentoSelicService(RestTemplate restTemplate, SelicRepository selicRepository, SelicMapper selicMapper) {
        this.restTemplate = restTemplate;
        this.selicRepository = selicRepository;
        this.selicMapper = selicMapper;
    }

    @Scheduled(fixedDelay = 24, timeUnit = TimeUnit.HOURS)
    public void salvaSelicNoBancoMensalmente() {
        LocalDate dataAtual = LocalDate.now();
        String dataAtualFormatada = dataAtual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String api = String.format("https://api.bcb.gov.br/dados/serie/bcdata.sgs.4390/dados?formato=json&dataInicial=%s&dataFinal=%s", dataAtualFormatada, dataAtualFormatada);
        SelicRequestDTO[] dto = restTemplate.getForObject(api, SelicRequestDTO[].class);

        if (dto != null && dto.length > 0) {
            SelicModel selicModel = selicMapper.mapToModel(dto[0]);
            if (!selicRepository.existsByData(selicModel.getData())) {
                selicRepository.save(selicModel);
                log.info("Salvando Selic no banco de dados");
            }
            else{
                log.info("Selic já existe no banco de dados");
            }
        }
    }
}