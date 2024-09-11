package br.gov.agu.samir.new_samir_back.mapper;

import br.gov.agu.samir.new_samir_back.dtos.BeneficioInacumulavelResponseDTO;
import br.gov.agu.samir.new_samir_back.dtos.BeneficioResponseDTO;
import br.gov.agu.samir.new_samir_back.dtos.BeneficioRequestDTO;
import br.gov.agu.samir.new_samir_back.models.BeneficioInacumulavelModel;
import br.gov.agu.samir.new_samir_back.models.BeneficioModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BeneficioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "beneficiosInacumulaveis",ignore = true)
    BeneficioModel toModel(BeneficioRequestDTO beneficioRequestDTO);



    BeneficioResponseDTO toResponseDTO(BeneficioModel beneficioModel);

    default List<BeneficioInacumulavelResponseDTO> toListResponseInacumulavelDTO(List<BeneficioInacumulavelModel> beneficioInacumulavelModels){
        return beneficioInacumulavelModels.stream().map(beneficioInacumulavelModel -> BeneficioInacumulavelResponseDTO.builder()
                .id(beneficioInacumulavelModel.getId())
                .nome(beneficioInacumulavelModel.getNome())
                .build()).toList();
    }

}
