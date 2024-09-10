package br.gov.agu.samir.new_samir_back.mapper;

import br.gov.agu.samir.new_samir_back.dtos.BeneficioInacumulavelRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.BeneficioInacumulavelResponseDTO;

import br.gov.agu.samir.new_samir_back.models.BeneficioInacumulavelModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BeneficioInacumulavelMapper {

    @Mapping(target = "id", ignore = true)
    BeneficioInacumulavelModel toModel(BeneficioInacumulavelRequestDTO beneficioInacumulavelRequestDTO);

    BeneficioInacumulavelResponseDTO toResponseDTO(BeneficioInacumulavelModel beneficioInacumulavelModel);
}
