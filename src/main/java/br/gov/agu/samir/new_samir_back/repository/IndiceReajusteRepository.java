package br.gov.agu.samir.new_samir_back.repository;

import br.gov.agu.samir.new_samir_back.models.IndiceReajusteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface IndiceReajusteRepository extends JpaRepository<IndiceReajusteModel, Long> {

    Optional<IndiceReajusteModel> findByData(LocalDate data);

    IndiceReajusteModel findFirstByData(LocalDate data);
}
