package br.gov.agu.samir.new_samir_back.mapper;

import br.gov.agu.samir.new_samir_back.dtos.IpcaeRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.IpcaeResponseDTO;
import br.gov.agu.samir.new_samir_back.models.IpcaeModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface IpcaeMapper {

    @Mapping(target = "id", ignore = true)
    IpcaeModel mapToModel(IpcaeRequestDTO requestDTO);

    IpcaeResponseDTO mapModelToResponseDTO(IpcaeModel model);
}
