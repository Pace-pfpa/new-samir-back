package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.SelicRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.SelicResponseDTO;
import br.gov.agu.samir.new_samir_back.exceptions.ResourceNotFoundException;
import br.gov.agu.samir.new_samir_back.mapper.SelicMapper;
import br.gov.agu.samir.new_samir_back.models.SelicModel;
import br.gov.agu.samir.new_samir_back.repository.SelicRepository;
import org.springframework.stereotype.Service;

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
        return listModel.stream().map(selicModel -> mapper.mapModelToResponseDTO(selicModel)).toList();
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
}
