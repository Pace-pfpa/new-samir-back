package br.gov.agu.samir.new_samir_back.controller;

import br.gov.agu.samir.new_samir_back.dtos.request.BeneficioInacumulavelRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.response.BeneficioInacumulavelResponseDTO;
import br.gov.agu.samir.new_samir_back.service.BeneficioInacumulavelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/beneficio-inacumulavel")
public class BeneficioInacumulavelController {

    private final BeneficioInacumulavelService beneficioInacumulavelService;

    public BeneficioInacumulavelController(BeneficioInacumulavelService beneficioInacumulavelService) {
        this.beneficioInacumulavelService = beneficioInacumulavelService;
    }

    @PostMapping
    public ResponseEntity<BeneficioInacumulavelResponseDTO> salvarBeneficioInacumulavel(@RequestBody BeneficioInacumulavelRequestDTO requestDTO) {
        BeneficioInacumulavelResponseDTO responseDTO = beneficioInacumulavelService.salvarBeneficioInacumulavel(requestDTO);
        return ResponseEntity.created(URI.create("/beneficio-inacumulavel/" + responseDTO.getId())).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeneficioInacumulavelResponseDTO> buscarBeneficioInacumulavelPorId(@PathVariable Long id) {
        BeneficioInacumulavelResponseDTO responseDTO = beneficioInacumulavelService.buscarBeneficioInacumulavelPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarBeneficioInacumulavel(@PathVariable Long id, @RequestBody BeneficioInacumulavelRequestDTO requestDTO) {
        String response = beneficioInacumulavelService.atualizarBeneficioInacumulavel(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarBeneficioInacumulavel(@PathVariable Long id) {
        beneficioInacumulavelService.deletarBeneficioInacumulavel(id);
        return ResponseEntity.noContent().build();
    }



}
