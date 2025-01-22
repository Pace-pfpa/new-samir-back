package br.gov.agu.samir.new_samir_back.modules.salario_minimo.mapper;

import br.gov.agu.samir.new_samir_back.modules.salario_minimo.dto.SalarioMinimoRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.salario_minimo.dto.SalarioMinimoResponseDTO;
import br.gov.agu.samir.new_samir_back.modules.salario_minimo.model.SalarioMinimoModel;
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
