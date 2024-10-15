package br.gov.agu.samir.new_samir_back.controller;

import br.gov.agu.samir.new_samir_back.dtos.request.JurosRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.response.JurosResponseDTO;
import br.gov.agu.samir.new_samir_back.service.JurosService;
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
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/juros")
public class JurosController {

    private final JurosService service;

    public JurosController(JurosService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<JurosResponseDTO> salvarJuros(@RequestBody JurosRequestDTO requestDTO) {
        JurosResponseDTO responseDTO = service.salvarJuros(requestDTO);
        return ResponseEntity.created(URI.create("/Juros/" + responseDTO.getId())).body(responseDTO);
    }

    @PostMapping("importar-dados")
    public ResponseEntity<String> importarDadosJuros(@RequestBody List<JurosRequestDTO> listResquestDTO) {
        String respose = service.importarDadosJuros(listResquestDTO);
        return ResponseEntity.ok(respose);
    }

    @GetMapping
    public ResponseEntity<List<JurosResponseDTO>> buscarTodasJuros() {
        List<JurosResponseDTO> listResponse = service.buscarTodasJuros();
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JurosResponseDTO> buscarPorId(@PathVariable Long id) {
        JurosResponseDTO responseDTO = service.buscarPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("buscarPorData")
    public ResponseEntity<JurosResponseDTO> buscarPorData(@RequestParam int mes, @RequestParam int ano) {
        JurosResponseDTO responseDTO = service.buscarPorData(mes,ano);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("buscarPorDataIntervalo")
    public ResponseEntity<List<JurosResponseDTO>> buscarPorDataIntervalor(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
        List<JurosResponseDTO> listResponse = service.buscarPorDataIntervalo(dataInicio, dataFim);
        return ResponseEntity.ok(listResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JurosResponseDTO> atualizarJuros(@PathVariable Long id, @RequestBody JurosRequestDTO requestDTO) {
        JurosResponseDTO responseDTO = service.atualizarJuros(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarJuros(@PathVariable Long id) {
        service.deletarJuros(id);
        return ResponseEntity.noContent().build();
    }
}
