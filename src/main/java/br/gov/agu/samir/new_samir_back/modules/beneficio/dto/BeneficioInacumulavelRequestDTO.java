package br.gov.agu.samir.new_samir_back.modules.beneficio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficioInacumulavelRequestDTO {

    private String nome;

    private List<Long> beneficios;
}
