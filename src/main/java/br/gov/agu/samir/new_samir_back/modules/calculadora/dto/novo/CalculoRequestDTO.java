package br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculoRequestDTO {

    private String cpf;

    private String parteAutora;

    private String numeroProcessoJudicial;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate ajuizamento;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate citacao;

    private Integer percentualHonorarios;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate honoratiosAte;

    private Integer acordo;

    private boolean limitarAcordo;

    private Integer tipoJuros;

    private Integer tipoCorrecao;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate calculadoPara;

    private boolean alcada;

    private List<DevidoRequestDTO> devidos;

}
