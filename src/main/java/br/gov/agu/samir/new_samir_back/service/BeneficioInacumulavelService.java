package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.BeneficioInacumulavelRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.BeneficioInacumulavelResponseDTO;
import br.gov.agu.samir.new_samir_back.dtos.BeneficioRequestDTO;
import br.gov.agu.samir.new_samir_back.exceptions.ResourceNotFoundException;
import br.gov.agu.samir.new_samir_back.mapper.BeneficioInacumulavelMapper;
import br.gov.agu.samir.new_samir_back.models.BeneficioInacumulavelModel;
import br.gov.agu.samir.new_samir_back.repository.BeneficioInacumulavelRepository;
import org.springframework.stereotype.Service;

@Service
public class BeneficioInacumulavelService {

    private final BeneficioInacumulavelMapper mapper;

    private final BeneficioInacumulavelRepository repository;

    public BeneficioInacumulavelService(BeneficioInacumulavelMapper mapper, BeneficioInacumulavelRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public BeneficioInacumulavelResponseDTO salvarBeneficioInacumulavel(BeneficioInacumulavelRequestDTO requestDTO) {
        BeneficioInacumulavelModel model = mapper.toModel(requestDTO);
        repository.save(model);
        return mapper.toResponseDTO(model);
    }

    public BeneficioInacumulavelResponseDTO buscarBeneficioInacumulavelPorId(Long id) {
        BeneficioInacumulavelModel model = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Beneficio não encontrado"));
        return mapper.toResponseDTO(model);
    }


    public BeneficioInacumulavelResponseDTO atualizarBeneficioInacumulavel(Long id, BeneficioInacumulavelRequestDTO requestDTO) {
        BeneficioInacumulavelModel model = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Beneficio não encontrado"));
        BeneficioInacumulavelModel updatedModel = BeneficioInacumulavelModel.builder()
                .id(model.getId())
                .nome(requestDTO.getNome() != null ? requestDTO.getNome() : model.getNome())
                .build();
        return mapper.toResponseDTO(updatedModel);
    }

    public void deletarBeneficioInacumulavel(Long id) {
        repository.deleteById(id);
    }
}
