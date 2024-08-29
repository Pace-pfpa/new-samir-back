package br.gov.agu.samir.new_samir_back.mapper;

import br.gov.agu.samir.new_samir_back.dtos.SalarioMinimoRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.SalarioMinimoResponseDTO;
import br.gov.agu.samir.new_samir_back.models.SalarioMinimoModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SalarioMinimoMapper {

    @Mapping(target = "id", ignore = true)
    SalarioMinimoModel requestDtoToModel(SalarioMinimoRequestDTO requestDTO);

    SalarioMinimoResponseDTO modelToResponseDTO(SalarioMinimoModel model);
}
