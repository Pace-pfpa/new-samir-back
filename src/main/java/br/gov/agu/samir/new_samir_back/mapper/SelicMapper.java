package br.gov.agu.samir.new_samir_back.mapper;

import br.gov.agu.samir.new_samir_back.dtos.SelicRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.SelicResponseDTO;
import br.gov.agu.samir.new_samir_back.models.SelicModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SelicMapper {

    @Mapping(target = "id", ignore = true)
    SelicModel mapToModel(SelicRequestDTO requestDTO);

    SelicResponseDTO mapModelToResponseDTO(SelicModel model);
}
