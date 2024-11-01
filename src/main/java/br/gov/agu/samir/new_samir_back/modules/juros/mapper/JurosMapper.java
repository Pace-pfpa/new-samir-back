package br.gov.agu.samir.new_samir_back.modules.juros.mapper;

import br.gov.agu.samir.new_samir_back.modules.juros.dto.JurosRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.juros.dto.JurosResponseDTO;
import br.gov.agu.samir.new_samir_back.modules.juros.model.JurosModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface JurosMapper {

    @Mapping(target = "id", ignore = true)
    JurosModel mapToModel(JurosRequestDTO requestDTO);

    JurosResponseDTO mapModelToResponseDTO(JurosModel model);
}
