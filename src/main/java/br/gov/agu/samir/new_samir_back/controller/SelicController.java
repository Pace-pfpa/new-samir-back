package br.gov.agu.samir.new_samir_back.controller;

import br.gov.agu.samir.new_samir_back.dtos.request.SelicRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.response.SelicResponseDTO;
import br.gov.agu.samir.new_samir_back.service.SelicService;
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
@RequestMapping("/selic")
public class SelicController {

    private final SelicService service;

    public SelicController(SelicService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SelicResponseDTO> salvarSelic(@RequestBody SelicRequestDTO requestDTO) {
        SelicResponseDTO responseDTO = service.salvarSelic(requestDTO);
        return ResponseEntity.created(URI.create("/selic/" + responseDTO.getId())).body(responseDTO);
    }

    @PostMapping("importar-dados")
    public ResponseEntity<String> importarDadosSelic(@RequestBody List<SelicRequestDTO> listResquestDTO) {
        String respose = service.importarDadosSelic(listResquestDTO);
        return ResponseEntity.ok(respose);
    }

    @GetMapping
    public ResponseEntity<List<SelicResponseDTO>> buscarTodasSelic() {
        List<SelicResponseDTO> listResponse = service.buscarTodasSelic();
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SelicResponseDTO> buscarPorId(@PathVariable Long id) {
        SelicResponseDTO responseDTO = service.buscarPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("buscarPorData")
    public ResponseEntity<SelicResponseDTO> buscarPorData(@RequestParam int mes, @RequestParam int ano) {
        SelicResponseDTO responseDTO = service.buscarPorData(mes,ano);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("buscarPorDataIntervalo")
    public ResponseEntity<List<SelicResponseDTO>> buscarPorDataIntervalor(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
        List<SelicResponseDTO> listResponse = service.buscarPorDataIntervalo(dataInicio, dataFim);
        return ResponseEntity.ok(listResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SelicResponseDTO> atualizarSelic(@PathVariable Long id, @RequestBody SelicRequestDTO requestDTO) {
        SelicResponseDTO responseDTO = service.atualizarSelic(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSelic(@PathVariable Long id) {
        service.deletarSelic(id);
        return ResponseEntity.noContent().build();
    }
}
