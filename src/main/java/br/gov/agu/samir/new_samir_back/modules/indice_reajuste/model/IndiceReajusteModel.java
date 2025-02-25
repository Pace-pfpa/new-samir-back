package br.gov.agu.samir.new_samir_back.modules.indice_reajuste.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne
    @JoinColumn(name = "data_reajuste_id", nullable = false)
    private DataReajusteModel dataReajuste;
}
