package br.gov.agu.samir.new_samir_back.modules.ipcae.controller;

import br.gov.agu.samir.new_samir_back.modules.ipcae.dto.IpcaeRequestDTO;
import br.gov.agu.samir.new_samir_back.modules.ipcae.dto.IpcaeResponseDTO;
import br.gov.agu.samir.new_samir_back.modules.ipcae.service.IpcaeService;
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
@RequestMapping("/ipcae")
public class IpcaeController {

    private final IpcaeService service;

    public IpcaeController(IpcaeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<IpcaeResponseDTO> salvarIpcae(@RequestBody IpcaeRequestDTO requestDTO) {
        IpcaeResponseDTO responseDTO = service.salvarIpcae(requestDTO);
        return ResponseEntity.created(URI.create("/Ipcae/" + responseDTO.getId())).body(responseDTO);
    }

    @PostMapping("importar-dados")
    public ResponseEntity<String> importarDadosIpcae(@RequestBody List<IpcaeRequestDTO> listResquestDTO) {
        String response = service.importarDadosIpcae(listResquestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<IpcaeResponseDTO>> buscarTodasIpcae() {
        List<IpcaeResponseDTO> listResponse = service.buscarTodasIpcae();
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IpcaeResponseDTO> buscarPorId(@PathVariable Long id) {
        IpcaeResponseDTO responseDTO = service.buscarPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/buscarPorData")
    public ResponseEntity<IpcaeResponseDTO> buscarPorData(@RequestParam int mes, @RequestParam int ano) {
        IpcaeResponseDTO responseDTO = service.buscarPorData(mes,ano);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/buscarPorDataIntervalo")
    public ResponseEntity<List<IpcaeResponseDTO>> buscarPorDataIntervalo(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
        List<IpcaeResponseDTO> listResponse = service.buscarPorDataIntervalo(dataInicio, dataFim);
        return ResponseEntity.ok(listResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IpcaeResponseDTO> atualizarIpcae(@PathVariable Long id, @RequestBody IpcaeRequestDTO requestDTO) {
        IpcaeResponseDTO responseDTO = service.atualizarIpcae(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarIpcae(@PathVariable Long id) {
        service.deletarIpcae(id);
        return ResponseEntity.noContent().build();
    }
}
