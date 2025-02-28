package br.gov.agu.samir.new_samir_back.modules.calculadora.controller;

import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.dto.CalculoResponseDTO;
import br.gov.agu.samir.new_samir_back.modules.calculadora.service.CalculoService;
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


    private final CalculoService calculoService;

    @PostMapping
    public ResponseEntity<CalculoResponseDTO> calcularMemoriaDeCalculo(@RequestBody CalculoRequestDTO requestDTO){
        CalculoResponseDTO responseDTO = calculoService.calculoProcesso(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

}
