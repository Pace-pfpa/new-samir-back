package br.gov.agu.samir.new_samir_back.modules.selic.service;

import br.gov.agu.samir.new_samir_back.modules.selic.dto.SelicRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.selic.mapper.SelicMapper;
import br.gov.agu.samir.new_samir_back.modules.selic.repository.SelicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Service
@Slf4j
@RequiredArgsConstructor
public class AgendamentoSelicService {

    private final WebClient webClient;
    private final SelicRepository selicRepository;
    private final SelicMapper selicMapper;

    @Scheduled(cron = "0 0 0 15 * ?")
    private void salvarSelicMensalmente2() {
        LocalDate dataAtual = LocalDate.now();
        String dataAtualFormatada = dataAtual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));


        SelicRequestDTO dto = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("formato", "json")
                        .queryParam("dataInicial", dataAtualFormatada)
                        .queryParam("dataFinal", dataAtualFormatada)
                        .build())
                .retrieve()
                .bodyToFlux(SelicRequestDTO.class)
                .blockFirst();

        assert dto != null;
        if (!selicRepository.existsByData(dto.getData())) {
            selicRepository.save(selicMapper.mapToModel(dto));
            log.info("Salvando Selic no banco de dados");
        } else {
            log.info("Selic já existe no banco de dados");
        }

    }
}