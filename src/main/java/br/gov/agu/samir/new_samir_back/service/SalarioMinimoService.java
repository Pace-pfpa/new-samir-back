package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.SalarioMinimoRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.SalarioMinimoResponseDTO;
import br.gov.agu.samir.new_samir_back.exceptions.ResourceNotFoundException;
import br.gov.agu.samir.new_samir_back.mapper.SalarioMinimoMapper;
import br.gov.agu.samir.new_samir_back.models.SalarioMinimoModel;
import br.gov.agu.samir.new_samir_back.repository.SalarioMinimoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;


@Service
public class SalarioMinimoService {

    private final SalarioMinimoRepository repository;

    private final SalarioMinimoMapper mapper;


    public SalarioMinimoService(SalarioMinimoRepository repository, SalarioMinimoMapper mapper, DateTimeFormatter formatter) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public SalarioMinimoResponseDTO salvarSalarioMinimo(SalarioMinimoRequestDTO requestDTO) {
        SalarioMinimoModel model = mapper.requestDtoToModel(requestDTO);
        repository.save(model);
        return mapper.modelToResponseDTO(model);
    }

    public SalarioMinimoResponseDTO getSalarioMinimoById(Long id) {
        SalarioMinimoModel model = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("não foi encontrado um salario minimo com id: " + id));
        return mapper.modelToResponseDTO(model);
    }

    public SalarioMinimoResponseDTO getSalarioMinimoByAno(int mes, int ano) {
        SalarioMinimoModel model = repository.findByMesAndAno(mes, ano);
        return mapper.modelToResponseDTO(model);
    }

    public SalarioMinimoResponseDTO updateSalarioMinimo(Long id, SalarioMinimoRequestDTO requestDTO) {
        SalarioMinimoModel model = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("não foi encontrado um salario minimo com id: " + id));

        SalarioMinimoModel modelUpdated = SalarioMinimoModel.builder()
                .id(model.getId())
                .data(requestDTO.getData() != null ? requestDTO.getData() : model.getData()) // Se valor do request vier diferente de null atualiza valor se não mantem valor antigo
                .valor(requestDTO.getValor() != null ? requestDTO.getValor() : model.getValor())
                .build();

        repository.save(modelUpdated);
        return mapper.modelToResponseDTO(modelUpdated);
    }

    public void deleteSalarioMinimo(Long id) {
        repository.deleteById(id);
    }

    public BigDecimal verificaSeRmiIsInferiorSalarioMinimo(CalculoRequestDTO calculoRequestDTO) {
        return calculoRequestDTO.getRmi().compareTo(getSalarioMinimoByAno(calculoRequestDTO.getDib().getMonthValue(), calculoRequestDTO.getDib().getYear()).getValor()) < 0
                ? getSalarioMinimoByAno(calculoRequestDTO.getDib().getMonthValue(), calculoRequestDTO.getDib().getYear()).getValor() : calculoRequestDTO.getRmi();
    }
}
