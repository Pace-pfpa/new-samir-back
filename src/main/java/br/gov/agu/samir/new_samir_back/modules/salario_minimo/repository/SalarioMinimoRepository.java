package br.gov.agu.samir.new_samir_back.modules.salario_minimo.repository;

import br.gov.agu.samir.new_samir_back.modules.salario_minimo.model.SalarioMinimoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;


@Repository
public interface SalarioMinimoRepository extends JpaRepository<SalarioMinimoModel, Long> {

   @Query(nativeQuery = true, value = "SELECT * FROM tb_salario_minimo WHERE EXTRACT(MONTH FROM data) = :mes AND EXTRACT(YEAR FROM data) = :ano")
   SalarioMinimoModel findByMesAndAno(@Param("mes") int mes, @Param("ano") int ano);

   /** Query que busca o salário mais próximo da data informada dentro do mesmo ano **/
   @Query("SELECT s FROM SalarioMinimoModel s WHERE YEAR(s.data) = :ano AND s.data <= :data ORDER BY s.data DESC LIMIT 1")
   Optional<SalarioMinimoModel> findSalarioMinimoProximoPorDataNoMesmoAno(@Param("data") LocalDate data, @Param("ano") int ano);

}
