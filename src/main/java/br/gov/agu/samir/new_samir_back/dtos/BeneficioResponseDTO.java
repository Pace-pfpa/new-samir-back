package br.gov.agu.samir.new_samir_back.dtos;

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
public class BeneficioResponseDTO {
    private Long id;
    private String nome;
    private Boolean dif;
    private Boolean decimoTerceiro;
    private List<BeneficioInacumulavelResponseDTO> beneficiosInacumulaveis;
}
