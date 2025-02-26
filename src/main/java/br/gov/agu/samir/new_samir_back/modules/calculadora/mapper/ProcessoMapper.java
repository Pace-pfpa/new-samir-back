package br.gov.agu.samir.new_samir_back.modules.calculadora.mapper;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo.ProcessoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProcessoMapper {

    @Mapping(target = "autor.nome", source = "parteAutora")
    @Mapping(target = "autor.cpf", source = "cpf")
    ProcessoDTO gerarProcessoDTO(CalculoRequestDTO requestDTO);

}
