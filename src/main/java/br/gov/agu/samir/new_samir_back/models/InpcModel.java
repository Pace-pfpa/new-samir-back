package br.gov.agu.samir.new_samir_back.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_inpc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InpcModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate data;

    private BigDecimal valor;
}