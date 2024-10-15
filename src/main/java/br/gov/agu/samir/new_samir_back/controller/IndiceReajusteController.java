package br.gov.agu.samir.new_samir_back.controller;

import br.gov.agu.samir.new_samir_back.dtos.request.IndiceReajusteRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.response.IndiceReajusteResponseDTO;
import br.gov.agu.samir.new_samir_back.service.IndiceReajusteService;
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
@RequestMapping("/indice-reajuste")
public class IndiceReajusteController {

    private final IndiceReajusteService service;

    public IndiceReajusteController(IndiceReajusteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<IndiceReajusteResponseDTO> salvarIndiceReajuste(@RequestBody IndiceReajusteRequestDTO requestDTO){
        IndiceReajusteResponseDTO responseDTO = service.salvarIndiceReajuste(requestDTO);
        return ResponseEntity.created(URI.create("/indice-reajuste/" + responseDTO.getId())).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IndiceReajusteResponseDTO> buscarIndiceReajustePorId(@PathVariable Long id){
        IndiceReajusteResponseDTO responseDTO = service.buscarIndiceReajustePorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IndiceReajusteResponseDTO> atualizarIndiceReajuste(@PathVariable Long id, @RequestBody IndiceReajusteRequestDTO requestDTO){
        IndiceReajusteResponseDTO responseDTO = service.atualizarIndiceReajuste(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarIndiceReajuste(@PathVariable Long id){
        service.deletarIndiceReajuste(id);
        return ResponseEntity.noContent().build();
    }
}
