package br.gov.agu.samir.new_samir_back.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_juros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JurosModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    @Column(precision = 38, scale = 4)
    private BigDecimal valor;
}