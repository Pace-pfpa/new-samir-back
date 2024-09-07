package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.SelicRequestDTO;
import br.gov.agu.samir.new_samir_back.mapper.SelicMapper;
import br.gov.agu.samir.new_samir_back.models.SelicModel;
import br.gov.agu.samir.new_samir_back.repository.SelicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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

    @Scheduled(cron = "0 0 0 * * ?")
    public void salvaSelicNoBancoMensalmente() {
        LocalDate dataAtual = LocalDate.now();
        String dataAtualFormatada = dataAtual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String api = String.format("https://api.bcb.gov.br/dados/serie/bcdata.sgs.4390/dados?formato=json&dataInicial=%s&dataFinal=%s", dataAtualFormatada, dataAtualFormatada);
        SelicRequestDTO[] dto = restTemplate.getForObject(api, SelicRequestDTO[].class);
        SelicModel selicModel = selicMapper.mapToModel(dto[0]);
        Optional<SelicModel> verificaSeExistente = selicRepository.findByData(selicModel.getData());
        if (verificaSeExistente.isEmpty()){
            selicRepository.save(selicModel);
            log.info("Salvando Selic no banco de dados");
        }
    }
}
