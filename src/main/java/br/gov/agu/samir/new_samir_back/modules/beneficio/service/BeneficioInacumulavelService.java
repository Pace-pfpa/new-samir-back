package br.gov.agu.samir.new_samir_back.modules.beneficio.service;

import br.gov.agu.samir.new_samir_back.modules.beneficio.dto.BeneficioInacumulavelRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.beneficio.dto.BeneficioInacumulavelResponseDTO;
import br.gov.agu.samir.new_samir_back.exceptions.ResourceNotFoundException;
import br.gov.agu.samir.new_samir_back.modules.beneficio.mapper.BeneficioInacumulavelMapper;
import br.gov.agu.samir.new_samir_back.modules.beneficio.model.BeneficioInacumulavelModel;
import br.gov.agu.samir.new_samir_back.modules.beneficio.model.BeneficioModel;
import br.gov.agu.samir.new_samir_back.modules.beneficio.repository.BeneficioInacumulavelRepository;
import br.gov.agu.samir.new_samir_back.modules.beneficio.repository.BeneficioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficioInacumulavelService {

    private final BeneficioInacumulavelMapper mapper;

    private final BeneficioInacumulavelRepository InacumulavelRepository;

    private final BeneficioRepository beneficioRepository;

    public BeneficioInacumulavelService(BeneficioInacumulavelMapper mapper, BeneficioInacumulavelRepository inacumulavelRepository, BeneficioRepository beneficioRepository) {
        this.mapper = mapper;
        InacumulavelRepository = inacumulavelRepository;
        this.beneficioRepository = beneficioRepository;
    }

    public BeneficioInacumulavelResponseDTO salvarBeneficioInacumulavel(BeneficioInacumulavelRequestDTO requestDTO) {
        BeneficioInacumulavelModel model = mapper.toModel(requestDTO);
        List<BeneficioModel> beneficios = beneficioRepository.findAllById(requestDTO.getBeneficios());
        model.setBeneficios(beneficios);
        BeneficioInacumulavelModel savedModel = InacumulavelRepository.save(model);


        return mapper.toResponseDTO(savedModel);
    }


    public BeneficioInacumulavelResponseDTO buscarBeneficioInacumulavelPorId(Long id) {
        BeneficioInacumulavelModel model = InacumulavelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Benefício inacumulável não encontrado"));
        BeneficioInacumulavelResponseDTO responseDTO = mapper.toResponseDTO(model);
        return responseDTO;
    }

    public String atualizarBeneficioInacumulavel(Long id, BeneficioInacumulavelRequestDTO requestDTO) {
        BeneficioInacumulavelModel model = InacumulavelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Benefício inacumulável não encontrado"));
        BeneficioInacumulavelModel modelUpdated = BeneficioInacumulavelModel.builder()
                .id(model.getId())
                .nome(requestDTO.getNome())
                .beneficios(beneficioRepository.findAllById(requestDTO.getBeneficios()))
                .build();
        InacumulavelRepository.save(modelUpdated);
        return "Benefício inacumulável atualizado com sucesso";
    }

    public void deletarBeneficioInacumulavel(Long id) {
        InacumulavelRepository.deleteById(id);
    }
}
