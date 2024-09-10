package br.gov.agu.samir.new_samir_back.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficioResponseDTO {
    private Long id;
    private String nome;
    private Boolean diff;
    private Boolean decimoTerceiro;
        private List<BeneficioInacumulavelResponseDTO> beneficiosInacumulaveis;
}
