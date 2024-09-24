package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.CalculoResponseDTO;
import br.gov.agu.samir.new_samir_back.service.factory.CorrecaoMonetariaFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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


    public List<CalculoResponseDTO> calculoSemBeneficioAcumulado(CalculoRequestDTO infoCalculo) {

        List<CalculoResponseDTO> tabela = new ArrayList<>();

        List<String> datas = gerarListaDatasService.gerarListaDatas(infoCalculo);

        BigDecimal indiceReajuste = BigDecimal.ONE;

        BigDecimal rmiConversavada = infoCalculo.getRmi();

        for(String data : datas) {


            if (isDataDeReajuste(data)){
                BigDecimal valorRmi = calculoRmiService.calcularRmi(rmiConversavada, data);
                BigDecimal indiceReajusteAnual = isPrimeiroReajuste(infoCalculo, data) ? calculoIndiceReajusteService.primeiroReajuste(infoCalculo) : calculoIndiceReajusteService.comumReajuste(data);
                rmiConversavada = valorRmi.multiply(indiceReajusteAnual);
            }

            BigDecimal rmi = calculoRmiService.calcularRmi(rmiConversavada, data);

            CalculoResponseDTO linha = new CalculoResponseDTO();

            linha.setData(data);

            linha.setIndiceReajusteDevido(indiceReajuste);

            BigDecimal devido = rmi.multiply(indiceReajuste).setScale(2, RoundingMode.HALF_UP);

            linha.setDevido(devido);

            linha.setIndiceReajusteRecebido(indiceReajuste);

            BigDecimal valorRecebido = BigDecimal.ZERO;

            linha.setRecebido(valorRecebido);

            BigDecimal diferenca = devido.subtract(valorRecebido).setScale(2, RoundingMode.HALF_UP);

            linha.setDiferenca(diferenca);

            BigDecimal indiceCorrecaoMonetaria = correcaoMonetariaFactory.getCalculo(infoCalculo.getTipoCorrecao()).calcularIndexadorCorrecaoMonetaria(data);

            linha.setIndiceCorrecaoMonetaria(indiceCorrecaoMonetaria);

            BigDecimal corrigido = diferenca.multiply(indiceCorrecaoMonetaria).setScale(2, RoundingMode.HALF_UP);

            linha.setSalarioCorrigido(corrigido);

            BigDecimal porcentagemJuros = BigDecimal.ZERO;

            linha.setPorcentagemJuros(porcentagemJuros);

            BigDecimal juros = corrigido.multiply(porcentagemJuros).setScale(2, RoundingMode.HALF_UP);

            linha.setJuros(juros);

            BigDecimal soma = corrigido.add(juros).setScale(2, RoundingMode.HALF_UP);

            linha.setSoma(soma);

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
