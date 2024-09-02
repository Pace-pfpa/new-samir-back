package br.gov.agu.samir.new_samir_back.repository;

import br.gov.agu.samir.new_samir_back.models.SelicModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SelicRepository extends JpaRepository<SelicModel,Long> {

    @Query(value = "SELECT * FROM tb_selic WHERE EXTRACT(MONTH FROM data) = :mes AND EXTRACT(YEAR FROM data) = :ano", nativeQuery = true)
    Optional<SelicModel> findByMesAndAno(@Param("mes") int mes,@Param("ano") int ano);

    @Query("SELECT s FROM SelicModel s WHERE s.data BETWEEN :dataInicio AND :dataFim")
    List<SelicModel> findAllByDataBetween(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}
