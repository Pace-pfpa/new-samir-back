package br.gov.agu.samir.new_samir_back.modules.calculadora.dto;

import br.gov.agu.samir.new_samir_back.modules.beneficio.dto.BeneficioAcumuladoRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.beneficio.enums.BeneficiosEnum;
import br.gov.agu.samir.new_samir_back.modules.calculadora.enums.TipoCorrecaoMonetaria;
import br.gov.agu.samir.new_samir_back.modules.calculadora.enums.TipoJuros;
import br.gov.agu.samir.new_samir_back.modules.beneficio.model.BeneficioInacumulavelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalculadoraRequestDTO {

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    @NotBlank(message = "A data de ajuizamento é um componente obrigatório")
    private LocalDate dataAjuizamento;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    @NotNull(message = "O data dib é um componente obrigatório")
    private LocalDate dib;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    @NotNull(message = "A data de inicio é um componente obrigatório")
    private LocalDate dataInicio;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    @NotNull(message = "A data final é um componente obrigatório")
    private LocalDate dataFim;

    @NotNull(message = "O rmi é um componente obrigatório")
    private BigDecimal rmi;

    @Min(value = 0, message = "A porcentagem do rmi não pode ser menor que 0")
    @Max(value = 100, message = "A porcentagem do rmi não pode ser maior que 100")
    private Integer porcentagemRmi = 100;

    @Min(value = 0 , message = "O acordo não pode ser menor que 0")
    @Max(value = 100, message = "O acordo não pode ser maior que 100")
    private Integer acordo = 100;

    private Integer porcentagemHonorarios;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate honorariosAdvocaticiosAte;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    @NotNull(message = "A data de inicio dos juros é um componente obrigatório")
    private LocalDate dataIncioJuros; // Data citação -> vai ter juros quando a citação for anterior a 12/2021

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O tipo de juros é um componente obrigatório")
    private int tipoJuros;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O tipo de correção monetária é um componente obrigatório")
    private int tipoCorrecao;

    @JsonFormat(pattern = "MM/yyyy", shape = JsonFormat.Shape.STRING)
    @NotNull(message = "A data de atualização é um componente obrigatório")
    private LocalDate atualizarAte;

    @NotNull(message = "O benefício é um componente obrigatório")
    private String beneficio;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    @NotNull(message = "A data de inicio do benefício é um componente obrigatório")
    private LocalDate dibAnterior;

    private boolean alcada;

    private boolean decimoTerceiroFinalCalculo;

    private List<BeneficioAcumuladoRequestDTO> beneficioAcumulados;


    public BigDecimal getRmi(){
        return rmi.multiply(BigDecimal.valueOf(this.porcentagemRmi)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
    }


}