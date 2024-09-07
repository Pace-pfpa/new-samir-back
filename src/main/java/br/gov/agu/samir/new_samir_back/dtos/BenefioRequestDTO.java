package br.gov.agu.samir.new_samir_back.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BenefioRequestDTO {
    private String nome;
    private Boolean diff;
    private Boolean decimoTerceiro;
    private List<Long> beneficiosInacumulaveisIds;
}
