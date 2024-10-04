package br.gov.agu.samir.new_samir_back.repository;

import br.gov.agu.samir.new_samir_back.models.SalarioMinimoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


@Repository
public interface SalarioMinimoRepository extends JpaRepository<SalarioMinimoModel, Long> {

   @Query(nativeQuery = true, value = "SELECT * FROM tb_salario_minimo WHERE EXTRACT(MONTH FROM data) = :mes AND EXTRACT(YEAR FROM data) = :ano")
   SalarioMinimoModel findByMesAndAno(@Param("mes") int mes, @Param("ano") int ano);

   SalarioMinimoModel findByData(LocalDate data);
}
