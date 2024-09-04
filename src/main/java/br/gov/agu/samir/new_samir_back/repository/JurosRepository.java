package br.gov.agu.samir.new_samir_back.repository;

import br.gov.agu.samir.new_samir_back.models.JurosModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JurosRepository extends JpaRepository<JurosModel,Long> {

    @Query(value = "SELECT * FROM tb_juros WHERE EXTRACT(MONTH FROM data) = :mes AND EXTRACT(YEAR FROM data) = :ano", nativeQuery = true)
    Optional<JurosModel> findByMesAndAno(@Param("mes") int mes,@Param("ano") int ano);

    @Query("SELECT s FROM JurosModel s WHERE s.data BETWEEN :dataInicio AND :dataFim")
    List<JurosModel> findAllByDataBetween(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}
