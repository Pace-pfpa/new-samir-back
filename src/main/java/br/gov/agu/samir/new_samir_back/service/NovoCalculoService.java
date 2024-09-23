package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.CalculoResponseDTO;
import br.gov.agu.samir.new_samir_back.service.factory.CorrecaoMonetariaFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class NovoCalculoService {

    private final GerarListaDatasService gerarListaDatasService;

    private final CalculoRmiService calculoRmiService;

    private final CorrecaoMonetariaFactory correcaoMonetariaFactory;

    private final CalculoIndiceReajusteService calculoIndiceReajusteService;

    private final CalculoJurosService calculoJurosService;


    public List<CalculoResponseDTO> calculoSemBeneficioAcumulado(CalculoRequestDTO infoCalculo){

        List<CalculoResponseDTO> tabela = new ArrayList<>();

        List<String> datas = gerarListaDatasService.gerarListaDatas(infoCalculo);

        BigDecimal valorRmi = calculoRmiService.calcularRmi(infoCalculo.getRmi(), datas.get(0));

        BigDecimal indiceReajuste = BigDecimal.ONE;



        for (String data : datas){

            BigDecimal devido = valorRmi.multiply(indiceReajuste);

            BigDecimal recebido = BigDecimal.ZERO;

            BigDecimal diferenca = devido.subtract(recebido);
            BigDecimal indiceCorrecaMonetaria = correcaoMonetariaFactory.getCalculo(infoCalculo.getTipoCorrecao()).calcularIndexadorCorrecaoMonetaria(data);


            BigDecimal salarioCorrigido = diferenca.multiply(indiceCorrecaMonetaria);

            BigDecimal porcentagemjuros = isCalculoComJuros(infoCalculo) ? calculoJurosService.calcularJuros(infoCalculo, data) : BigDecimal.ZERO;
            BigDecimal juros = salarioCorrigido.multiply(porcentagemjuros);


            if (isDataDeReajuste(data)){

                if (isPrimeiroReajuste(infoCalculo, data)) {
                    indiceReajuste = calculoIndiceReajusteService.primeiroReajuste(infoCalculo);
                }
                valorRmi = calculoRmiService.calcularRmi(valorRmi, data);
                indiceReajuste = calculoIndiceReajusteService.comumReajuste(data);
            }

            CalculoResponseDTO linha = CalculoResponseDTO.builder()
                    .data(data)
                    .indiceReajusteDevido(indiceReajuste)
                    .devido(devido)
                    .indiceReajusteRecebido(indiceReajuste)
                    .recebido(recebido)
                    .diferenca(diferenca)
                    .indiceCorrecaoMonetaria(BigDecimal.ZERO)
                    .salarioCorrigido(salarioCorrigido)
                    .porcentagemJuros(porcentagemjuros)
                    .juros(juros)
                    .soma(salarioCorrigido.add(juros)).build();

            tabela.add(linha);

        }

        return tabela;
    }



    private boolean isCalculoComJuros(CalculoRequestDTO infoCalculo){
        return infoCalculo.getDataIncioJuros() != null ||
                infoCalculo.getDataIncioJuros().isBefore(LocalDate.of(2021,12,1));
    }

    private boolean isDataDeReajuste(String data){
        return data.split("/")[1].equals("01") && data.split("/")[0].equals("01");
    }

    private boolean isPrimeiroReajuste(CalculoRequestDTO infoCalculo, String data){
        int ano = Integer.parseInt(data.split("/")[2]);
        int mes = Integer.parseInt(data.split("/")[1]);

        return  ano == infoCalculo.getDib().plusYears(1).getYear() && mes == 1;

    }
}
