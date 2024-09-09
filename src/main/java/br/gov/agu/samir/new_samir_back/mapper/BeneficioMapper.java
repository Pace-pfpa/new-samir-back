package br.gov.agu.samir.new_samir_back.mapper;

import br.gov.agu.samir.new_samir_back.dtos.BeneficioResponseDTO;
import br.gov.agu.samir.new_samir_back.dtos.BeneficioRequestDTO;
import br.gov.agu.samir.new_samir_back.models.BeneficioModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;




@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BeneficioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "beneficiosInacumulaveis",ignore = true)
    BeneficioModel toModel(BeneficioRequestDTO beneficioRequestDTO);


    @Mapping(target = "beneficiosInacumulaveis",ignore = true)
    BeneficioResponseDTO toResponseDTO(BeneficioModel beneficioModel);

}
