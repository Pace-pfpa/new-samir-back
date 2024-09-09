package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.BeneficioRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.BeneficioResponseDTO;
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

    }



}
