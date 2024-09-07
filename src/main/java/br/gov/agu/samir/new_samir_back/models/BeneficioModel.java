package br.gov.agu.samir.new_samir_back.models;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "tb_beneficio")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeneficioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Boolean diff;

    private Boolean decimoTerceiro;
}