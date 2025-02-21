package br.gov.agu.samir.new_samir_back.modules.beneficio.controller;

import br.gov.agu.samir.new_samir_back.modules.beneficio.dto.BeneficioRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.beneficio.dto.BeneficioResponseDTO;
import br.gov.agu.samir.new_samir_back.modules.beneficio.model.BeneficioModel;
import br.gov.agu.samir.new_samir_back.modules.beneficio.repository.BeneficioRepository;
import br.gov.agu.samir.new_samir_back.modules.beneficio.service.BeneficioService;
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
@RequestMapping("/beneficio")
public class BeneficioController {


    private final BeneficioService beneficioService;

    private final BeneficioRepository repository;

    public BeneficioController(BeneficioService beneficioService, BeneficioRepository repository) {
        this.beneficioService = beneficioService;
        this.repository = repository;
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

    @GetMapping("/buscar")
    public ResponseEntity<BeneficioModel> buscarPorNome(@RequestParam String nome){
        BeneficioModel beneficio = repository.findByNome(nome);
        return ResponseEntity.ok(beneficio);
    }
}
