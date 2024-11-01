package br.gov.agu.samir.new_samir_back.modules.selic.mapper;

import br.gov.agu.samir.new_samir_back.modules.selic.dto.SelicRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.selic.dto.SelicResponseDTO;
import br.gov.agu.samir.new_samir_back.modules.selic.model.SelicModel;
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
