package br.gov.agu.samir.new_samir_back.modules.calculadora.controller;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.CalculadoraResponseDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.CalculadoraRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.CalculadoraService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculo")
@AllArgsConstructor
public class CalculadoraController {

    private final CalculadoraService calculadoraService;

    @PostMapping
    public ResponseEntity<CalculadoraResponseDTO> calcularMemoriaDeCalculo( @RequestBody CalculadoraRequestDTO requestDTO){
        CalculadoraResponseDTO responseDTO = calculadoraService.calcularProcesso(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
