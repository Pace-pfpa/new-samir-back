package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.IpcaeRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.IpcaeResponseDTO;
import br.gov.agu.samir.new_samir_back.exceptions.ResourceNotFoundException;
import br.gov.agu.samir.new_samir_back.mapper.IpcaeMapper;
import br.gov.agu.samir.new_samir_back.models.IpcaeModel;
import br.gov.agu.samir.new_samir_back.repository.IpcaeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IpcaeService {

    private final IpcaeRepository repository;

    private final IpcaeMapper mapper;

    public IpcaeService(IpcaeRepository repository, IpcaeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public IpcaeResponseDTO salvarIpcae(IpcaeRequestDTO requestDTO) {
        IpcaeModel model = mapper.mapToModel(requestDTO);
        repository.save(model);
        return mapper.mapModelToResponseDTO(model);
    }

    public List<IpcaeResponseDTO> buscarTodasIpcae() {
        List<IpcaeModel> listModel = repository.findAll();
        return listModel.stream().map(IpcaeModel -> mapper.mapModelToResponseDTO(IpcaeModel)).toList();
    }

    public IpcaeResponseDTO buscarPorId(Long id) {
        IpcaeModel model = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("não foi encontrado uma taxa Ipcae com o id informado"));
        return mapper.mapModelToResponseDTO(model);
    }

    public List<IpcaeResponseDTO> buscarPorDataIntervalo(LocalDate dataInicio, LocalDate dataFim) {
        List<IpcaeModel> listModel = repository.findAllByDataBetween(dataInicio, dataFim);
        return listModel.stream().map(IpcaeModel -> mapper.mapModelToResponseDTO(IpcaeModel)).toList();
    }

    public IpcaeResponseDTO atualizarIpcae(Long id, IpcaeRequestDTO requestDTO) {
        IpcaeModel model = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("não foi encontrado uma taxa Ipcae com o id informado"));

        IpcaeModel modelUpdated = IpcaeModel.builder()
                .id(model.getId())
                .data(requestDTO.getData() != null ? requestDTO.getData() : model.getData()) // Se valor do request vier diferente de null atualiza valor se não mantem valor antigo
                .valor(requestDTO.getValor() != null ? requestDTO.getValor() : model.getValor())
                .build();

        repository.save(modelUpdated);
        return mapper.mapModelToResponseDTO(modelUpdated);
    }

    public void deletarIpcae(Long id) {
        repository.deleteById(id);
    }

    public IpcaeResponseDTO buscarPorData(int mes, int ano) {
        IpcaeModel model = repository.findByMesAndAno(mes, ano).orElseThrow(() -> new ResourceNotFoundException("não foi encontrado uma taxa Ipcae com o mês e ano informado"));
        return mapper.mapModelToResponseDTO(model);
    }


    public String importarDadosIpcae(List<IpcaeRequestDTO> listResquestDTO) {
        List<IpcaeModel> listModel = listResquestDTO.stream().map(IpcaeRequestDTO -> mapper.mapToModel(IpcaeRequestDTO)).toList();
        repository.saveAll(listModel);
        return "dados importados com sucesso";
    }
}
