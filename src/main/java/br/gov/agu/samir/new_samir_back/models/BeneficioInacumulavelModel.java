package br.gov.agu.samir.new_samir_back.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_beneficio_inacumulavel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficioInacumulavelModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
}
