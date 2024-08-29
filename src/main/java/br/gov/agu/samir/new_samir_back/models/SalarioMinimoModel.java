package br.gov.agu.samir.new_samir_back.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_salario_minimo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalarioMinimoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate data;

    private BigDecimal valor;
}
