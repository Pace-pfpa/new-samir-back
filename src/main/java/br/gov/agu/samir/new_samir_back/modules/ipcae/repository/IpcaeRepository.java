package br.gov.agu.samir.new_samir_back.modules.ipcae.repository;

import br.gov.agu.samir.new_samir_back.modules.ipcae.model.IpcaeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IpcaeRepository extends JpaRepository<IpcaeModel,Long> {

    @Query(value = "SELECT * FROM tb_ipca-e WHERE EXTRACT(MONTH FROM data) = :mes AND EXTRACT(YEAR FROM data) = :ano", nativeQuery = true)
    Optional<IpcaeModel> findByMesAndAno(@Param("mes") int mes,@Param("ano") int ano);

    @Query("SELECT i FROM IpcaeModel i WHERE i.data BETWEEN :dataInicio AND :dataFim")
    List<IpcaeModel> findAllByDataBetween(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}
