package br.gov.agu.samir.new_samir_back.service.strategy;

import br.gov.agu.samir.new_samir_back.dtos.CalculoRequestDTO;

import java.time.LocalDate;
import java.util.List;

public interface GerarListaStrategy {

    List<String> gerarLista(CalculoRequestDTO requestDTO);
}
