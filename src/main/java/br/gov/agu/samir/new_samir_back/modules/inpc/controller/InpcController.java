package br.gov.agu.samir.new_samir_back.modules.inpc.controller;

import br.gov.agu.samir.new_samir_back.modules.inpc.dto.InpcRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.inpc.dto.InpcResponseDTO;
import br.gov.agu.samir.new_samir_back.modules.inpc.service.InpcService;
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
@RequestMapping("/inpc")
public class InpcController {

    private final InpcService service;

    public InpcController(InpcService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InpcResponseDTO> salvarInpc(@RequestBody InpcRequestDTO requestDTO) {
        InpcResponseDTO responseDTO = service.salvarInpc(requestDTO);
        return ResponseEntity.created(URI.create("/Inpc/" + responseDTO.getId())).body(responseDTO);
    }

    @PostMapping("importar-dados")
    public ResponseEntity<String> importarDadosInpc(@RequestBody List<InpcRequestDTO> listResquestDTO) {
        String response = service.importarDadosInpc(listResquestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<InpcResponseDTO>> buscarTodasInpc() {
        List<InpcResponseDTO> listResponse = service.buscarTodasInpc();
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InpcResponseDTO> buscarPorId(@PathVariable Long id) {
        InpcResponseDTO responseDTO = service.buscarPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("buscarPorData")
    public ResponseEntity<InpcResponseDTO> buscarPorData(@RequestParam int mes, @RequestParam int ano) {
        InpcResponseDTO responseDTO = service.buscarPorData(mes,ano);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/buscarPorDataIntervalo")
    public ResponseEntity<List<InpcResponseDTO>> buscarPorDataIntervalo(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
        List<InpcResponseDTO> listResponse = service.buscarPorDataIntervalo(dataInicio, dataFim);
        return ResponseEntity.ok(listResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InpcResponseDTO> atualizarInpc(@PathVariable Long id, @RequestBody InpcRequestDTO requestDTO) {
        InpcResponseDTO responseDTO = service.atualizarInpc(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarInpc(@PathVariable Long id) {
        service.deletarInpc(id);
        return ResponseEntity.noContent().build();
    }
}
