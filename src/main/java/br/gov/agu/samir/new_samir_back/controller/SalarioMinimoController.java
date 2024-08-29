package br.gov.agu.samir.new_samir_back.controller;

import br.gov.agu.samir.new_samir_back.dtos.SalarioMinimoRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.SalarioMinimoResponseDTO;
import br.gov.agu.samir.new_samir_back.service.SalarioMinimoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/salario-minimo")
public class SalarioMinimoController {

    private final SalarioMinimoService service;

    public SalarioMinimoController(SalarioMinimoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SalarioMinimoResponseDTO> saveSalarioMinimo(@RequestBody SalarioMinimoRequestDTO requestDTO) {
        SalarioMinimoResponseDTO responseDTO = service.salvarSalarioMinimo(requestDTO);
        return  ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalarioMinimoResponseDTO> getSalarioMinimoById(@PathVariable Long id) {
        SalarioMinimoResponseDTO responseDTO = service.getSalarioMinimoById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<SalarioMinimoResponseDTO> getSalarioMinimoByAno(@RequestParam Integer ano) {
        SalarioMinimoResponseDTO responseDTO = service.getSalarioMinimoByAno(ano);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalarioMinimoResponseDTO> updateSalarioMinimo(@PathVariable Long id, @RequestBody SalarioMinimoRequestDTO requestDTO) {
        SalarioMinimoResponseDTO responseDTO = service.updateSalarioMinimo(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalarioMinimo(@PathVariable Long id) {
        service.deleteSalarioMinimo(id);
        return ResponseEntity.noContent().build();
    }
}
