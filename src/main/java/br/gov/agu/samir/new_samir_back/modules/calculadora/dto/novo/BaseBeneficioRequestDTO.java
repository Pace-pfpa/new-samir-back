package br.gov.agu.samir.new_samir_back.modules.calculadora.dto.novo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseBeneficioRequestDTO {

    private String especie;

    private String nb;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dib;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataInicial;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataFinal;

    private LocalDate nbAnterior;

    private BigDecimal rmi;

    private Integer porcentagemRmi;

    private boolean decimoTerceiroFimCalculo;

    public BigDecimal getRmi() {
        return rmi.multiply(BigDecimal.valueOf(porcentagemRmi))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}