package br.gov.agu.samir.new_samir_back.mapper;

import br.gov.agu.samir.new_samir_back.dtos.request.IndiceReajusteRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.response.IndiceReajusteResponseDTO;
import br.gov.agu.samir.new_samir_back.models.IndiceReajusteModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface IndiceReajusteMapper {

    @Mapping(target = "id", ignore = true)
    IndiceReajusteModel mapToModel(IndiceReajusteRequestDTO requestDTO);

    IndiceReajusteResponseDTO mapToResponseDTO(IndiceReajusteModel model);
}
