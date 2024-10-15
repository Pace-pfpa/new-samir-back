package br.gov.agu.samir.new_samir_back.controller;

import br.gov.agu.samir.new_samir_back.dtos.request.BeneficioAcumuladoRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.request.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.response.CalculoResponseDTO;
import br.gov.agu.samir.new_samir_back.models.BeneficioInacumulavelModel;
import br.gov.agu.samir.new_samir_back.repository.BeneficioRepository;
import br.gov.agu.samir.new_samir_back.service.CalculadoraService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/calculo")
@AllArgsConstructor
public class CalculadoraController {

    private final CalculadoraService calculadoraService;

    @PostMapping
    public ResponseEntity<List<CalculoResponseDTO>> calcular(@RequestBody CalculoRequestDTO requestDTO){
        List<CalculoResponseDTO> tabela = calculadoraService.gerarTabelaDeCalculo(requestDTO);
        return ResponseEntity.ok(tabela);
    }
}
