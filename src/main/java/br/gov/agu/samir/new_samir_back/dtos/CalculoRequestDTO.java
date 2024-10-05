package br.gov.agu.samir.new_samir_back.dtos;



import br.gov.agu.samir.new_samir_back.enums.BeneficiosEnum;
import br.gov.agu.samir.new_samir_back.enums.TipoCorrecaoMonetaria;
import br.gov.agu.samir.new_samir_back.enums.TipoJuros;
import br.gov.agu.samir.new_samir_back.models.BeneficioInacumulavelModel;
import br.gov.agu.samir.new_samir_back.models.BeneficioModel;
import br.gov.agu.samir.new_samir_back.repository.BeneficioRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalculoRequestDTO {

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate dib;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate dataFim;

    private BigDecimal rmi;

    private int porcentagemRmi;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate dataIncioJuros; //Data citação -> vai ter juros quando a citação for anterior a 12/2021

    @Enumerated(EnumType.STRING)
    private TipoJuros tipoJuros;

    @Enumerated(EnumType.STRING)
    private TipoCorrecaoMonetaria tipoCorrecao;

    private BeneficiosEnum beneficio;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate dibAnterior;

    private List<BeneficioAcumuladoRequestDTO> beneficioAcumulados;


    private BeneficioRepository beneficioRepository;


    public BigDecimal getRmi(){
        return this.rmi != null ? rmi.multiply(BigDecimal.valueOf(porcentagemRmi)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP) : getRmi();
    }

    public List<BeneficioAcumuladoRequestDTO> getBeneficioInacumulaveisParaCalculo() {
        List<BeneficioAcumuladoRequestDTO> beneficiosAcumulados = this.beneficioAcumulados;
        List<BeneficioInacumulavelModel> beneficiosInacumulaveis = beneficioRepository.findByNome(this.beneficio.getDescricao()).getBeneficiosInacumulaveis();

        return beneficiosAcumulados.stream()
                .filter(beneficioAcumulado -> beneficiosInacumulaveis.contains(beneficioAcumulado.getBeneficioAcumulado())).toList();
    }
}