package br.gov.agu.samir.new_samir_back.modules.indice_reajuste.repository;

import br.gov.agu.samir.new_samir_back.modules.indice_reajuste.model.IndiceReajusteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface IndiceReajusteRepository extends JpaRepository<IndiceReajusteModel, Long> {

    Optional<IndiceReajusteModel> findByData(LocalDate data);


    @Query("SELECT r FROM IndiceReajusteModel r WHERE r.dataReajuste.data = :dataReajuste ORDER BY r.data ASC LIMIT 1 ")
    IndiceReajusteModel findFirstByDataReajuste(LocalDate dataReajuste);
}
