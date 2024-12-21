package br.gov.agu.samir.new_samir_back.modules.indice_reajuste.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "tb_data_reajuste")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataReajusteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    @OneToMany(mappedBy = "dataReajuste")
    private List<IndiceReajusteModel> indicesReajuste;
}
