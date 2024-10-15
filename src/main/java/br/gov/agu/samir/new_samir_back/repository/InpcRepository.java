package br.gov.agu.samir.new_samir_back.repository;

import br.gov.agu.samir.new_samir_back.models.InpcModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InpcRepository extends JpaRepository<InpcModel,Long> {

    @Query(value = "SELECT * FROM tb_inpc WHERE EXTRACT(MONTH FROM data) = :mes AND EXTRACT(YEAR FROM data) = :ano", nativeQuery = true)
    Optional<InpcModel> findByMesAndAno(@Param("mes") int mes,@Param("ano") int ano);

    @Query("SELECT i FROM InpcModel i WHERE i.data BETWEEN :dataInicio AND :dataFim")
    List<InpcModel> findAllByDataBetween(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}
