package br.gov.agu.samir.new_samir_back.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficioInacumulavelResponseDTO {

    private Long id;

    private String nome;

    private List<BeneficioResponseDTO> beneficios;

}
