package br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ProcessoDTO {

    private String numeroProcessoJudicial;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate ajuizamento;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate citacao;

    private AutorDTO autor;

    @JsonFormat(pattern = "MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate calculadoraPara;

    private Integer percentualHonorarios;


}
