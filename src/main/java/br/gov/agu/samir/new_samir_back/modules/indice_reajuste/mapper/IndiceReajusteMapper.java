package br.gov.agu.samir.new_samir_back.modules.indice_reajuste.mapper;

import br.gov.agu.samir.new_samir_back.modules.indice_reajuste.dto.IndiceReajusteRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.indice_reajuste.dto.IndiceReajusteResponseDTO;
import br.gov.agu.samir.new_samir_back.modules.indice_reajuste.model.IndiceReajusteModel;
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
