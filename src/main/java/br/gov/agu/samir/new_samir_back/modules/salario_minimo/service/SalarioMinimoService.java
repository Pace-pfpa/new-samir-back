package br.gov.agu.samir.new_samir_back.modules.salario_minimo.service;

import br.gov.agu.samir.new_samir_back.modules.salario_minimo.dto.SalarioMinimoRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.salario_minimo.dto.SalarioMinimoResponseDTO;
import br.gov.agu.samir.new_samir_back.exceptions.ResourceNotFoundException;
import br.gov.agu.samir.new_samir_back.modules.salario_minimo.mapper.SalarioMinimoMapper;
import br.gov.agu.samir.new_samir_back.modules.salario_minimo.model.SalarioMinimoModel;
import br.gov.agu.samir.new_samir_back.modules.salario_minimo.repository.SalarioMinimoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    public BigDecimal getSalarioMinimoProximoPorDataNoMesmoAno(LocalDate data){
        int ano = data.getYear();

        return repository.findSalarioMinimoProximoPorDataNoMesmoAno(data, ano)
                .orElseThrow(() -> new ResourceNotFoundException("não foi encontrado um salario minimo para a data: " + data))
                .getValor();
    }
}
