package br.gov.agu.samir.new_samir_back.modules.calculadora.mapper;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.ProcessoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProcessoMapper {

    @Mapping(target = "autor.parteAutora", source = "parteAutora")
    @Mapping(target = "autor.cpf", source = "cpf")
    ProcessoDTO gerarProcessoDTO(CalculoRequestDTO requestDTO);

}
