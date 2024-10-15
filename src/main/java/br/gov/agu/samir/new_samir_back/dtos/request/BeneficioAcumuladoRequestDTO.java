package br.gov.agu.samir.new_samir_back.dtos.request;

import br.gov.agu.samir.new_samir_back.enums.BeneficiosEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeneficioAcumuladoRequestDTO {

    private BeneficiosEnum beneficioAcumulado;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate inicioDesconto;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate fimDesconto;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate dib;

    private BigDecimal rmi;

    private Integer porcentagemRmi;

    private LocalDate dibAnterior;

    public BigDecimal getRmi(){
        int porcentagem = porcentagemRmi != null ? porcentagemRmi : 100;
        if (porcentagem < 0 || porcentagem > 100){
            throw new IllegalArgumentException("Porcentagem RMI deve ser entre 0 e 100");
        }
        return rmi.multiply(BigDecimal.valueOf(porcentagem)).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
    }
}
