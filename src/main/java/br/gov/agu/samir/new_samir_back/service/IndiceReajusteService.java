package br.gov.agu.samir.new_samir_back.service;


import br.gov.agu.samir.new_samir_back.dtos.request.IndiceReajusteRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.response.IndiceReajusteResponseDTO;
import br.gov.agu.samir.new_samir_back.exceptions.ResourceNotFoundException;
import br.gov.agu.samir.new_samir_back.mapper.IndiceReajusteMapper;
import br.gov.agu.samir.new_samir_back.models.IndiceReajusteModel;
import br.gov.agu.samir.new_samir_back.repository.IndiceReajusteRepository;
import org.springframework.stereotype.Service;

@Service
public class IndiceReajusteService {

    private final IndiceReajusteRepository repository;

    private final IndiceReajusteMapper mapper;

    public IndiceReajusteService(IndiceReajusteRepository repository, IndiceReajusteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public IndiceReajusteResponseDTO salvarIndiceReajuste(IndiceReajusteRequestDTO requestDTO) {
        IndiceReajusteModel model = mapper.mapToModel(requestDTO);
        repository.save(model);
        return mapper.mapToResponseDTO(model);
    }

    public IndiceReajusteResponseDTO buscarIndiceReajustePorId(Long id) {
        IndiceReajusteModel model = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("não foi encontrado um índice de reajuste com o id informado"));
        return mapper.mapToResponseDTO(model);
    }

    public IndiceReajusteResponseDTO atualizarIndiceReajuste(Long id, IndiceReajusteRequestDTO requestDTO) {
        IndiceReajusteModel model = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("não foi encontrado um índice de reajuste com o id informado"));
        IndiceReajusteModel modelUpdated = IndiceReajusteModel.builder()
                .id(model.getId())
                .data(requestDTO.getData() != null ? requestDTO.getData() : model.getData())
                .valor(requestDTO.getValor() != null ? requestDTO.getValor() : model.getValor())
                .build();

        repository.save(modelUpdated);
        return mapper.mapToResponseDTO(model);
    }

    public void deletarIndiceReajuste(Long id) {
        repository.deleteById(id);
    }
}
