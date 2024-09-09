package br.gov.agu.samir.new_samir_back.repository;

import br.gov.agu.samir.new_samir_back.models.BeneficioInacumulavelModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficioInacumulavelRepository extends JpaRepository<BeneficioInacumulavelModel,Long> {
}
