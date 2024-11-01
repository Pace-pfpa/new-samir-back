package br.gov.agu.samir.new_samir_back.modules.inpc.service;

import br.gov.agu.samir.new_samir_back.modules.inpc.dto.InpcRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.inpc.dto.InpcResponseDTO;
import br.gov.agu.samir.new_samir_back.exceptions.ResourceNotFoundException;
import br.gov.agu.samir.new_samir_back.modules.inpc.mapper.InpcMapper;
import br.gov.agu.samir.new_samir_back.modules.inpc.model.InpcModel;
import br.gov.agu.samir.new_samir_back.modules.inpc.repository.InpcRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public List<InpcResponseDTO> buscarPorDataIntervalo(LocalDate dataInicio, LocalDate dataFim) {
        List<InpcModel> listModel = repository.findAllByDataBetween(dataInicio, dataFim);
        return listModel.stream().map(InpcModel -> mapper.mapModelToResponseDTO(InpcModel)).toList();
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


    public String importarDadosInpc(List<InpcRequestDTO> listResquestDTO) {
        List<InpcModel> listModel = listResquestDTO.stream().map(InpcRequestDTO -> mapper.mapToModel(InpcRequestDTO)).toList();
        repository.saveAll(listModel);
        return "dados importados com sucesso";
    }
}
