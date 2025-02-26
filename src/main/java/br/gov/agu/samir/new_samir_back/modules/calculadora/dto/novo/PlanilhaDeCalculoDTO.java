package br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.RendimentosAcumuladosIRDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class PlanilhaDeCalculoDTO {

    private String especie;// numero e nb

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dib;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dibAnterior;

    private BigDecimal rmi;

    private Integer porcentagemRmi;

    private String periodo;

    private List<CompetenciaDTO> competencias;

    private RendimentosAcumuladosIRDTO rendimentosRecebidos;

}
