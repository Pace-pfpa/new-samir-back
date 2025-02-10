package br.gov.agu.samir.new_samir_back.modules.calculadora.dto;

import br.gov.agu.samir.new_samir_back.modules.calculadora.enums.TipoCorrecaoMonetaria;
import br.gov.agu.samir.new_samir_back.modules.calculadora.enums.TipoJuros;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    @Min(value = 0 , message = "O acordo não pode ser menor que 0")
    @Max(value = 100, message = "O acordo não pode ser maior que 100")
    private Integer acordo = 100;

    private Integer porcentagemHonorarios;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate honorariosAdvocaticiosAte;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    @NotNull(message = "A data de inicio dos juros é um componente obrigatório")
    private LocalDate citacao; // Data citação -> vai ter juros quando a citação for anterior a 12/2021

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O tipo de juros é um componente obrigatório")
    private TipoJuros tipoJuros;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O tipo de correção monetária é um componente obrigatório")
    private TipoCorrecaoMonetaria tipoCorrecao;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    @NotNull(message = "A data de atualização é um componente obrigatório")
    private LocalDate atualizarAte;

    private boolean alcada;

    private boolean decimoTerceiroFinalCalculo;

    private List<BeneficioDevidoRequestDTO> beneficiosDevidos;

    private List<BeneficioRecebidoRequestDTO> beneficioAcumulados;

}