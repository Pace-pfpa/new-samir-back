package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.SalarioMinimoRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.SalarioMinimoResponseDTO;
import br.gov.agu.samir.new_samir_back.mapper.SalarioMinimoMapper;
import br.gov.agu.samir.new_samir_back.models.SalarioMinimoModel;
import br.gov.agu.samir.new_samir_back.repository.SalarioMinimoRepository;
import org.springframework.stereotype.Service;

@Service
public class SalarioMinimoService {

    private final SalarioMinimoRepository repository;

    private final SalarioMinimoMapper mapper;

    public SalarioMinimoService(SalarioMinimoRepository repository, SalarioMinimoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public SalarioMinimoResponseDTO salvarSalarioMinimo(SalarioMinimoRequestDTO requestDTO) {
        SalarioMinimoModel model = mapper.requestDtoToModel(requestDTO);
        repository.save(model);
        return mapper.modelToResponseDTO(model);
    }

    public SalarioMinimoResponseDTO getSalarioMinimoById(Long id) {
        SalarioMinimoModel model = repository.findById(id).orElseThrow(RuntimeException::new);
        return mapper.modelToResponseDTO(model);
    }

    public SalarioMinimoResponseDTO getSalarioMinimoByAno(Integer ano) {
        SalarioMinimoModel model = repository.findSalarioMinimoModelByData_Year(ano).orElseThrow(RuntimeException::new);
        return mapper.modelToResponseDTO(model);
    }

    public SalarioMinimoResponseDTO updateSalarioMinimo(Long id, SalarioMinimoRequestDTO requestDTO) {
        SalarioMinimoModel model = repository.findById(id).orElseThrow(RuntimeException::new);
        model.setData(requestDTO.getData());
        model.setValor(requestDTO.getValor());
        repository.save(model);
        return mapper.modelToResponseDTO(model);
    }

    public void deleteSalarioMinimo(Long id) {
        repository.deleteById(id);
    }
}
