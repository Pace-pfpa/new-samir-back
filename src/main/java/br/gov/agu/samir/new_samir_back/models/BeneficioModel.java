package br.gov.agu.samir.new_samir_back.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "tb_beneficio")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BeneficioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Boolean diff;

    private Boolean decimoTerceiro;
}
