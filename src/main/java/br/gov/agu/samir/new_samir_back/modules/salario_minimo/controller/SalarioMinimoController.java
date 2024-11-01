package br.gov.agu.samir.new_samir_back.modules.salario_minimo.controller;

import br.gov.agu.samir.new_samir_back.modules.salario_minimo.dto.SalarioMinimoRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.salario_minimo.dto.SalarioMinimoResponseDTO;
import br.gov.agu.samir.new_samir_back.modules.salario_minimo.service.SalarioMinimoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
        return  ResponseEntity.created(URI.create("/salario-minimo/" + responseDTO.getId())).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalarioMinimoResponseDTO> getSalarioMinimoById(@PathVariable Long id) {
        SalarioMinimoResponseDTO responseDTO = service.getSalarioMinimoById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<SalarioMinimoResponseDTO> getSalarioMinimoByAno(@RequestParam int mes, @RequestParam int ano) {
        SalarioMinimoResponseDTO responseDTO = service.getSalarioMinimoByAno(mes,ano);
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
