package br.gov.agu.samir.new_samir_back.mapper;

import br.gov.agu.samir.new_samir_back.dtos.request.BeneficioInacumulavelRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.response.BeneficioInacumulavelResponseDTO;

import br.gov.agu.samir.new_samir_back.dtos.response.BeneficioResponseDTO;
import br.gov.agu.samir.new_samir_back.models.BeneficioInacumulavelModel;
import br.gov.agu.samir.new_samir_back.models.BeneficioModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BeneficioInacumulavelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "beneficios", ignore = true)
    BeneficioInacumulavelModel toModel(BeneficioInacumulavelRequestDTO beneficioInacumulavelRequestDTO);


    BeneficioInacumulavelResponseDTO toResponseDTO(BeneficioInacumulavelModel beneficioInacumulavelModel);

    default List<BeneficioResponseDTO> toBeneficioResponseList(List<BeneficioModel> beneficios) {
        return beneficios.stream()
                .map(beneficio -> BeneficioResponseDTO.builder()
                        .id(beneficio.getId())
                        .nome(beneficio.getNome())
                        .build())
                .toList();
    }
}
