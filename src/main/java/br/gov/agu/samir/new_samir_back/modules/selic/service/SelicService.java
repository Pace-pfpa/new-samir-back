package br.gov.agu.samir.new_samir_back.modules.selic.service;

import br.gov.agu.samir.new_samir_back.modules.selic.dto.SelicRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.selic.dto.SelicResponseDTO;
import br.gov.agu.samir.new_samir_back.exceptions.ResourceNotFoundException;
import br.gov.agu.samir.new_samir_back.modules.selic.mapper.SelicMapper;
import br.gov.agu.samir.new_samir_back.modules.selic.model.SelicModel;
import br.gov.agu.samir.new_samir_back.modules.selic.repository.SelicRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SelicService {

    private final SelicRepository repository;

    private final SelicMapper mapper;

    public SelicService(SelicRepository repository, SelicMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public SelicResponseDTO salvarSelic(SelicRequestDTO requestDTO) {
        SelicModel model = mapper.mapToModel(requestDTO);
        repository.save(model);
        return mapper.mapModelToResponseDTO(model);
    }

    public List<SelicResponseDTO> buscarTodasSelic() {
        List<SelicModel> listModel = repository.findAll();
        return listModel.stream().map(mapper::mapModelToResponseDTO).toList();
    }

    public SelicResponseDTO buscarPorId(Long id) {
        SelicModel model = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("não foi encontrado uma taxa selic com o id informado"));
        return mapper.mapModelToResponseDTO(model);
    }

    public SelicResponseDTO atualizarSelic(Long id, SelicRequestDTO requestDTO) {
        SelicModel model = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("não foi encontrado uma taxa selic com o id informado"));

        SelicModel modelUpdated = SelicModel.builder()
                .id(model.getId())
                .data(requestDTO.getData() != null ? requestDTO.getData() : model.getData()) // Se valor do request vier diferente de null atualiza valor se não mantem valor antigo
                .valor(requestDTO.getValor() != null ? requestDTO.getValor() : model.getValor())
                .build();

        repository.save(modelUpdated);
        return mapper.mapModelToResponseDTO(modelUpdated);
    }

    public void deletarSelic(Long id) {
        repository.deleteById(id);
    }

    public SelicResponseDTO buscarPorData(int mes, int ano) {
        SelicModel model = repository.findByMesAndAno(mes, ano).orElseThrow(() -> new ResourceNotFoundException("não foi encontrado uma taxa selic com o mês e ano informado"));
        return mapper.mapModelToResponseDTO(model);
    }

    public String importarDadosSelic(List<SelicRequestDTO> listResquestDTO) {
        List<SelicModel> listModel = listResquestDTO.stream().map(mapper::mapToModel).toList();
        repository.saveAll(listModel);
        return "dados importados com sucesso";
    }

    public List<SelicResponseDTO> buscarPorDataIntervalo(LocalDate dataInicio, LocalDate dataFim) {
        List<SelicModel> listModel = repository.findAllByDataBetween(dataInicio, dataFim);
        return listModel.stream().map(mapper::mapModelToResponseDTO).toList();
    }
}
