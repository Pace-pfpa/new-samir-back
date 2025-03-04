package br.gov.agu.samir.new_samir_back.modules.indice_reajuste.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
