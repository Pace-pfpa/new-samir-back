package br.gov.agu.samir.new_samir_back.dtos;

import lombok.*;


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
