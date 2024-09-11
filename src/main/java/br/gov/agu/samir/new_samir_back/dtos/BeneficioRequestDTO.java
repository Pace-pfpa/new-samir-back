package br.gov.agu.samir.new_samir_back.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficioRequestDTO {
    private String nome;
    private Boolean dif;
    private Boolean decimoTerceiro;
    private List<Long> beneficiosInacumulaveisIds;
}