package br.gov.agu.samir.new_samir_back.modules.juros.service;

import br.gov.agu.samir.new_samir_back.modules.juros.dto.JurosRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.juros.dto.JurosResponseDTO;
import br.gov.agu.samir.new_samir_back.exceptions.ResourceNotFoundException;
import br.gov.agu.samir.new_samir_back.modules.juros.mapper.JurosMapper;
import br.gov.agu.samir.new_samir_back.modules.juros.model.JurosModel;
import br.gov.agu.samir.new_samir_back.modules.juros.repository.JurosRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class JurosService {

    private final JurosRepository repository;

    private final JurosMapper mapper;

    public JurosService(JurosRepository repository, JurosMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public JurosResponseDTO salvarJuros(JurosRequestDTO requestDTO) {
        JurosModel model = mapper.mapToModel(requestDTO);
        repository.save(model);
        return mapper.mapModelToResponseDTO(model);
    }

    public List<JurosResponseDTO> buscarTodasJuros() {
        List<JurosModel> listModel = repository.findAll();
        return listModel.stream().map(JurosModel -> mapper.mapModelToResponseDTO(JurosModel)).toList();
    }

    public JurosResponseDTO buscarPorId(Long id) {
        JurosModel model = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("não foi encontrado uma taxa JurosService com o id informado"));
        return mapper.mapModelToResponseDTO(model);
    }

    public JurosResponseDTO atualizarJuros(Long id, JurosRequestDTO requestDTO) {
        JurosModel model = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("não foi encontrado uma taxa JurosService com o id informado"));

        JurosModel modelUpdated = JurosModel.builder()
                .id(model.getId())
                .data(requestDTO.getData() != null ? requestDTO.getData() : model.getData()) // Se valor do request vier diferente de null atualiza valor se não mantem valor antigo
                .valor(requestDTO.getValor() != null ? requestDTO.getValor() : model.getValor())
                .build();

        repository.save(modelUpdated);
        return mapper.mapModelToResponseDTO(modelUpdated);
    }

    public void deletarJuros(Long id) {
        repository.deleteById(id);
    }

    public JurosResponseDTO buscarPorData(int mes, int ano) {
        JurosModel model = repository.findByMesAndAno(mes, ano).orElseThrow(() -> new ResourceNotFoundException("não foi encontrado uma taxa JurosService com o mês e ano informado"));
        return mapper.mapModelToResponseDTO(model);
    }

    public String importarDadosJuros(List<JurosRequestDTO> listResquestDTO) {
        List<JurosModel> listModel = listResquestDTO.stream().map(JurosRequestDTO -> mapper.mapToModel(JurosRequestDTO)).toList();
        repository.saveAll(listModel);
        return "dados importados com sucesso";
    }

    public List<JurosResponseDTO> buscarPorDataIntervalo(LocalDate dataInicio, LocalDate dataFim) {
        List<JurosModel> listModel = repository.findAllByDataBetween(dataInicio, dataFim);
        return listModel.stream().map(JurosModel -> mapper.mapModelToResponseDTO(JurosModel)).toList();
    }
}
