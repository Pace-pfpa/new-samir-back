package br.gov.agu.samir.new_samir_back.modules.beneficio.repository;

import br.gov.agu.samir.new_samir_back.modules.beneficio.model.BeneficioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficioRepository extends JpaRepository<BeneficioModel, Long> {

    BeneficioModel findByNome(String nome);
}
