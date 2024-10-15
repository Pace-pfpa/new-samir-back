package br.gov.agu.samir.new_samir_back.mapper;

import br.gov.agu.samir.new_samir_back.dtos.request.InpcRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.response.InpcResponseDTO;
import br.gov.agu.samir.new_samir_back.models.InpcModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface InpcMapper {

    @Mapping(target = "id", ignore = true)
    InpcModel mapToModel(InpcRequestDTO requestDTO);

    InpcResponseDTO mapModelToResponseDTO(InpcModel model);
}
