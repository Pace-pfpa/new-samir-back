package br.gov.agu.samir.new_samir_back.modules.beneficio.controller;

import br.gov.agu.samir.new_samir_back.modules.beneficio.dto.BeneficioInacumulavelRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.beneficio.dto.BeneficioInacumulavelResponseDTO;
import br.gov.agu.samir.new_samir_back.modules.beneficio.service.BeneficioInacumulavelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
