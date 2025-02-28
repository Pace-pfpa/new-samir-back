package br.gov.agu.samir.new_samir_back.modules.calculadora.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
public class DevidoRequestDTO extends BaseBeneficioRequestDTO {

    private List<RecebidoRequestDTO> recebidos;

}
