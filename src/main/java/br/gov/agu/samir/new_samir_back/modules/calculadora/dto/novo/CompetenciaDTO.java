package br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor @NoArgsConstructor
public class CompetenciaDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate data;

    private BigDecimal indiceReajusteDevido;

    private BigDecimal devido;

    private BigDecimal indiceReajusteRecebido;

    private BigDecimal recebido;

    private BigDecimal diferenca;

    private BigDecimal indiceCorrecaoMonetaria;

    private BigDecimal salarioCorrigido;

    private BigDecimal porcentagemJuros;

    private BigDecimal juros;

    private BigDecimal soma;


}
