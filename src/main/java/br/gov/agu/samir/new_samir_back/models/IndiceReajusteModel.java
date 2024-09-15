package br.gov.agu.samir.new_samir_back.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_indice_reajuste")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndiceReajusteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(precision = 30, scale = 4, nullable = false)
    private BigDecimal valor;
}
