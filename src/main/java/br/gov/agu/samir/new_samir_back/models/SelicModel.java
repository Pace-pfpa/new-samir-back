package br.gov.agu.samir.new_samir_back.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_selic")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SelicModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    private BigDecimal valor;
}
