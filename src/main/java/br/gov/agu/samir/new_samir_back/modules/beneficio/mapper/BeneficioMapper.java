package br.gov.agu.samir.new_samir_back.modules.beneficio.mapper;

import br.gov.agu.samir.new_samir_back.modules.beneficio.dto.BeneficioInacumulavelResponseDTO;
import br.gov.agu.samir.new_samir_back.modules.beneficio.dto.BeneficioResponseDTO;
import br.gov.agu.samir.new_samir_back.modules.beneficio.dto.BeneficioRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.beneficio.model.BeneficioInacumulavelModel;
import br.gov.agu.samir.new_samir_back.modules.beneficio.model.BeneficioModel;
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
