package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.request.BeneficioAcumuladoRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.request.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.dtos.response.CalculoResponseDTO;
import br.gov.agu.samir.new_samir_back.models.BeneficioInacumulavelModel;
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
public class CalculoService {

    private final GerarListaDatasService gerarListaDatasService;

    private final CalculoRmiService calculoRmiService;

    private final CorrecaoMonetariaFactory correcaoMonetariaFactory;

    private final CalculoIndiceReajusteService calculoIndiceReajusteService;

    private final CalculoJurosService calculoJurosService;
    
    private final DecimoTerceiroService decimoTerceiroService;

    private final SalarioMinimoService salarioMinimoService;

    private static final String MES_DECIMO_TERCEIRO = "13";

    private static final String MES_REAJUSTE = "01";

    private static final String DIA_REAJUSTE = "01";



    public List<CalculoResponseDTO> calculoSemBeneficioAcumulado(CalculoRequestDTO infoCalculo) {

        List<CalculoResponseDTO> tabela = new ArrayList<>();

        List<String> datas = gerarListaDeDatasParaCalculo(infoCalculo);

        BigDecimal indiceReajuste = BigDecimal.ONE;

        BigDecimal salarioMinimoEpoca = retornaSalarioMaisMinimoProximoPorDataNoMesmoAno(infoCalculo.getDib());

        BigDecimal rmiConversavada = isRmiMenorSalarioMinimo(infoCalculo.getRmi(), salarioMinimoEpoca) ? salarioMinimoEpoca : infoCalculo.getRmi();

        for(String data : datas) {

            if(!isDecimoTerceiro(data)){

                if (isDataDeReajuste(data)){
                    BigDecimal valorRmi = calculoRmiService.calcularRmi(rmiConversavada, data);
                    BigDecimal indiceReajusteAnual = isPrimeiroReajuste(infoCalculo, data) ? calcularPrimeiroReajuste(infoCalculo) : calculoIndiceReajusteService.comumReajuste(data);
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

                BigDecimal indiceCorrecaoMonetaria = retornaCorrecaoMonetaia(data, infoCalculo);

                linha.setIndiceCorrecaoMonetaria(indiceCorrecaoMonetaria);

                BigDecimal corrigido = diferenca.multiply(indiceCorrecaoMonetaria).setScale(2, RoundingMode.HALF_UP);

                linha.setSalarioCorrigido(corrigido);

                BigDecimal porcentagemJuros = isCalculoComJuros(infoCalculo) ? calculoJurosService.calcularJuros(infoCalculo,data) : BigDecimal.ZERO;

                linha.setPorcentagemJuros(porcentagemJuros);

                BigDecimal juros = corrigido.multiply(porcentagemJuros).setScale(2, RoundingMode.HALF_UP);

                linha.setJuros(juros);

                BigDecimal soma = corrigido.add(juros).setScale(2, RoundingMode.HALF_UP);

                linha.setSoma(soma);

                tabela.add(linha);
            } else{

                CalculoResponseDTO linhaDecimoTerceiro = new CalculoResponseDTO();

                linhaDecimoTerceiro.setData(data);

                linhaDecimoTerceiro.setIndiceReajusteDevido(BigDecimal.ONE);

                BigDecimal devido = decimoTerceiroService.calcularDecimoTerceiro(rmiConversavada,infoCalculo.getDib(),data);

                linhaDecimoTerceiro.setDevido(devido);

                linhaDecimoTerceiro.setIndiceReajusteRecebido(BigDecimal.ONE);

                linhaDecimoTerceiro.setRecebido(BigDecimal.ZERO);

                linhaDecimoTerceiro.setDiferenca(linhaDecimoTerceiro.getDevido());

                linhaDecimoTerceiro.setIndiceCorrecaoMonetaria(retornaCorrecaoMonetaia(data, infoCalculo));

                linhaDecimoTerceiro.setSalarioCorrigido(devido.multiply(linhaDecimoTerceiro.getIndiceCorrecaoMonetaria()).setScale(2, RoundingMode.HALF_UP));

                linhaDecimoTerceiro.setPorcentagemJuros(BigDecimal.ZERO);

                linhaDecimoTerceiro.setJuros(BigDecimal.ZERO);

                linhaDecimoTerceiro.setSoma(linhaDecimoTerceiro.getSalarioCorrigido());

                tabela.add(linhaDecimoTerceiro);
            }
        }
        return tabela;
    }

    public List<CalculoResponseDTO> calculoComBeneficioAcumulado(CalculoRequestDTO requestDTO) {

        return null;
    }

    private BigDecimal retornaCorrecaoMonetaia(String data, CalculoRequestDTO infoCalculo){
        return correcaoMonetariaFactory.getCalculo(infoCalculo.getTipoCorrecao()).calcularIndexadorCorrecaoMonetaria(data, infoCalculo.getAtualizarAte());
    }

    private BigDecimal calcularPrimeiroReajuste(CalculoRequestDTO infoCalculo){
        if (infoCalculo.getDibAnterior() != null){
            return calculoIndiceReajusteService.primeiroReajusteComDibAnterior(infoCalculo);
        }
        return calculoIndiceReajusteService.primeiroReajuste(infoCalculo);
    }

    private BigDecimal retornaSalarioMaisMinimoProximoPorDataNoMesmoAno(LocalDate data){
        return salarioMinimoService.getSalarioMinimoProximoPorDataNoMesmoAno(data);
    }

    private List<String> gerarListaDeDatasParaCalculo(CalculoRequestDTO infoCalculo){
        return gerarListaDatasService.gerarListaDatas(infoCalculo);
    }

    private boolean isRmiMenorSalarioMinimo(BigDecimal rmi, BigDecimal salarioMinimo){
        return rmi.compareTo(salarioMinimo) < 0;
    }

    private boolean isCalculoComJuros(CalculoRequestDTO infoCalculo){
        return infoCalculo.getDataIncioJuros() != null &&
                infoCalculo.getDataIncioJuros().isBefore(LocalDate.of(2021,12,1));
    }

    private boolean isDecimoTerceiro(String data){
        return data.split("/")[1].equals(MES_DECIMO_TERCEIRO);
    }

    private boolean isDataDeReajuste(String data){
        return data.split("/")[1].equals(DIA_REAJUSTE) && data.split("/")[0].equals(MES_REAJUSTE);
    }

    private boolean isPrimeiroReajuste(CalculoRequestDTO infoCalculo, String data){
        int ano = Integer.parseInt(data.split("/")[2]);
        int mes = Integer.parseInt(data.split("/")[1]);
        return  ano == infoCalculo.getDib().plusYears(1).getYear() && mes == 1;
    }
}
