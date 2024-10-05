package br.gov.agu.samir.new_samir_back.controller;

import br.gov.agu.samir.new_samir_back.dtos.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.CalculoResponseDTO;
import br.gov.agu.samir.new_samir_back.service.CalculoService;
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
public class CalculoController {

    private final CalculoService calculoService;

    @PostMapping
    public ResponseEntity<List<CalculoResponseDTO>> calcular(@RequestBody CalculoRequestDTO requestDTO){
        if (requestDTO.getBeneficioAcumulados().isEmpty()){
            List<CalculoResponseDTO> tabela = calculoService.calculoSemBeneficioAcumulado(requestDTO);
            return ResponseEntity.ok(tabela);
        }
        List<CalculoResponseDTO> tabela = calculoService.calculoComBeneficioAcumulado(requestDTO);
        return ResponseEntity.ok(tabela);

    }
}
