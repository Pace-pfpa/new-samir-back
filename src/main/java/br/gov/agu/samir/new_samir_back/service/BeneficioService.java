package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.request.BeneficioRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.response.BeneficioResponseDTO;
import br.gov.agu.samir.new_samir_back.exceptions.ResourceNotFoundException;
import br.gov.agu.samir.new_samir_back.mapper.BeneficioMapper;
import br.gov.agu.samir.new_samir_back.models.BeneficioInacumulavelModel;
import br.gov.agu.samir.new_samir_back.models.BeneficioModel;
import br.gov.agu.samir.new_samir_back.repository.BeneficioInacumulavelRepository;
import br.gov.agu.samir.new_samir_back.repository.BeneficioRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BeneficioService {

    private final BeneficioRepository beneficioRepository;

    private final BeneficioInacumulavelRepository inacumulavelRepository;

    private final BeneficioMapper mapper;

    public BeneficioService(BeneficioRepository beneficioRepository, BeneficioInacumulavelRepository inacumulavelRepository, BeneficioMapper mapper) {
        this.beneficioRepository = beneficioRepository;
        this.inacumulavelRepository = inacumulavelRepository;
        this.mapper = mapper;
    }

    public BeneficioResponseDTO salvarBeneficio(BeneficioRequestDTO requestDTO){
        BeneficioModel model = mapper.toModel(requestDTO);
        List<BeneficioInacumulavelModel> beneficioInacumulavelModels = inacumulavelRepository.findAllById(requestDTO.getBeneficiosInacumulaveisIds());
        model.setBeneficiosInacumulaveis(beneficioInacumulavelModels);
        beneficioRepository.save(model);

        BeneficioResponseDTO responseDTO = mapper.toResponseDTO(model);

        responseDTO.setBeneficiosInacumulaveis(mapper.toListResponseInacumulavelDTO(beneficioInacumulavelModels));

        return responseDTO;

    }

    public BeneficioResponseDTO buscarBeneficioPorId(Long id){
        BeneficioModel model = beneficioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Beneficio não encontrado"));
        BeneficioResponseDTO responseDTO = mapper.toResponseDTO(model);
        responseDTO.setBeneficiosInacumulaveis(mapper.toListResponseInacumulavelDTO(model.getBeneficiosInacumulaveis()));
        return responseDTO;
    }


    public String atualizarBeneficio(Long id, BeneficioRequestDTO requestDTO) {
        BeneficioModel model = beneficioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Beneficio não encontrado"));

        List<BeneficioInacumulavelModel> beneficioInacumulavelModels = inacumulavelRepository.findAllById(requestDTO.getBeneficiosInacumulaveisIds());

        BeneficioModel modelUpdated = BeneficioModel.builder().
                id(model.getId())
                .dif(requestDTO.getDif() != null ? requestDTO.getDif() : model.getDif())
                .decimoTerceiro(requestDTO.getDecimoTerceiro() != null ? requestDTO.getDecimoTerceiro() : model.getDecimoTerceiro())
                .nome(requestDTO.getNome() != null ? requestDTO.getNome() : model.getNome())
                .beneficiosInacumulaveis(requestDTO.getBeneficiosInacumulaveisIds() != null ? beneficioInacumulavelModels : model.getBeneficiosInacumulaveis())
                .build();

        beneficioRepository.save(modelUpdated);

        return "Beneficio atualizado com sucesso!";
    }

    public void deletarBeneficio(Long id) {
        beneficioRepository.deleteById(id);
    }
}
