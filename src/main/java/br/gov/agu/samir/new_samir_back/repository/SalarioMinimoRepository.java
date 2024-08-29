package br.gov.agu.samir.new_samir_back.repository;

import br.gov.agu.samir.new_samir_back.models.SalarioMinimoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SalarioMinimoRepository extends JpaRepository<SalarioMinimoModel, Long> {

    Optional<SalarioMinimoModel> findSalarioMinimoModelByData(LocalDate data);

    Optional<SalarioMinimoModel> findSalarioMinimoModelByData_Year(int year);
}
