package br.gov.agu.samir.new_samir_back.modules.calculadora.controller;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.CalculoResponseDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.CalculadoraService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calculo")
@AllArgsConstructor
public class CalculadoraController {

    private final CalculadoraService calculadoraService;

    @PostMapping
    public ResponseEntity<List<CalculoResponseDTO>> calcularMemoriaDeCalculo(@RequestBody CalculoRequestDTO requestDTO){
        List<CalculoResponseDTO> tabela = calculadoraService.gerarTabelaDeCalculo(requestDTO);
        return ResponseEntity.ok(tabela);
    }
}
