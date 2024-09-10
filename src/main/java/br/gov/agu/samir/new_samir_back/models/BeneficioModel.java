package br.gov.agu.samir.new_samir_back.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


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

    @ManyToMany
    @JoinTable(name = "tb_beneficio_inacumulavel",
            joinColumns = @JoinColumn(name = "beneficio_id"),
            inverseJoinColumns = @JoinColumn(name = "inacumulavel_id"))
    @JsonManagedReference
    private List<BeneficioInacumulavelModel> beneficiosInacumulaveis;
}
