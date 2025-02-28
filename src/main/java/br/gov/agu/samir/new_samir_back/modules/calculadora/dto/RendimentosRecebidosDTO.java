package br.gov.agu.samir.new_samir_back.modules.calculadora.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class RendimentosRecebidosDTO{

    private List<DiscriminacaoDTO> discriminacao;

}
