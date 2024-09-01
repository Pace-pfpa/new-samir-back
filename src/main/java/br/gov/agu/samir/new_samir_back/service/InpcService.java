package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.InpcRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.InpcResponseDTO;
import br.gov.agu.samir.new_samir_back.exceptions.ResourceNotFoundException;
import br.gov.agu.samir.new_samir_back.mapper.InpcMapper;
import br.gov.agu.samir.new_samir_back.models.InpcModel;
import br.gov.agu.samir.new_samir_back.repository.InpcRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InpcService {

    private final InpcRepository repository;

    private final InpcMapper mapper;

    public InpcService(InpcRepository repository, InpcMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public InpcResponseDTO salvarInpc(InpcRequestDTO requestDTO) {
        InpcModel model = mapper.mapToModel(requestDTO);
        repository.save(model);
        return mapper.mapModelToResponseDTO(model);
    }

    public List<InpcResponseDTO> buscarTodasInpc() {
        List<InpcModel> listModel = repository.findAll();
        return listModel.stream().map(InpcModel -> mapper.mapModelToResponseDTO(InpcModel)).toList();
    }

    public InpcResponseDTO buscarPorId(Long id) {
        InpcModel model = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("não foi encontrado uma taxa Inpc com o id informado"));
        return mapper.mapModelToResponseDTO(model);
    }

    public InpcResponseDTO atualizarInpc(Long id, InpcRequestDTO requestDTO) {
        InpcModel model = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("não foi encontrado uma taxa Inpc com o id informado"));

        InpcModel modelUpdated = InpcModel.builder()
                .id(model.getId())
                .data(requestDTO.getData() != null ? requestDTO.getData() : model.getData()) // Se valor do request vier diferente de null atualiza valor se não mantem valor antigo
                .valor(requestDTO.getValor() != null ? requestDTO.getValor() : model.getValor())
                .build();

        repository.save(modelUpdated);
        return mapper.mapModelToResponseDTO(modelUpdated);
    }

    public void deletarInpc(Long id) {
        repository.deleteById(id);
    }

    public InpcResponseDTO buscarPorData(int mes, int ano) {
        InpcModel model = repository.findByMesAndAno(mes, ano).orElseThrow(() -> new ResourceNotFoundException("não foi encontrado uma taxa Inpc com o mês e ano informado"));
        return mapper.mapModelToResponseDTO(model);
    }
}
