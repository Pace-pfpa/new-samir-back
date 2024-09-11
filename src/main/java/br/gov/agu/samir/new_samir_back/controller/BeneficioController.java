package br.gov.agu.samir.new_samir_back.controller;

import br.gov.agu.samir.new_samir_back.dtos.BeneficioRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.BeneficioResponseDTO;
import br.gov.agu.samir.new_samir_back.models.BeneficioModel;
import br.gov.agu.samir.new_samir_back.service.BeneficioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/beneficio")
public class BeneficioController {


    private final BeneficioService beneficioService;

    public BeneficioController(BeneficioService beneficioService) {
        this.beneficioService = beneficioService;
    }

    @PostMapping
    public ResponseEntity<BeneficioResponseDTO> salvarBeneficio(@RequestBody BeneficioRequestDTO requestDTO){
        BeneficioResponseDTO responseDTO = beneficioService.salvarBeneficio(requestDTO);
        return ResponseEntity.created(URI.create("/beneficio/"+responseDTO.getId())).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeneficioResponseDTO> buscarBeneficioPorId(@PathVariable Long id){
        BeneficioResponseDTO responseDTO = beneficioService.buscarBeneficioPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarBeneficio(@PathVariable Long id, @RequestBody BeneficioRequestDTO requestDTO){
        String response = beneficioService.atualizarBeneficio(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarBeneficio(@PathVariable Long id){
        beneficioService.deletarBeneficio(id);
        return ResponseEntity.noContent().build();
    }
}
