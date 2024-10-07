package br.gov.agu.samir.new_samir_back.service;

import br.gov.agu.samir.new_samir_back.dtos.request.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.enums.BeneficiosEnum;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class GerarListaDatasService {


   /**
 * Gera uma lista de datas com base nas informações de cálculo fornecidas.
 * Se o benefício não incluir décimo terceiro, gera uma lista sem décimo terceiro.
 * Caso contrário, gera uma lista com décimo terceiro.
 *
 * @param infoCalculo as informações de cálculo contendo a data de início (DIB) e a data de fim
 * @return uma lista de strings representando as datas no formato "dd/MM/yyyy"
 */
public List<String> gerarListaDatas(CalculoRequestDTO infoCalculo) {
    return isBeneficioSemDecimoTerceiro(infoCalculo) ?
            gerarListaSemDecimoTerceiro(infoCalculo.getDib(), infoCalculo.getDataFim()) :
            gerarListaComDecimoTerceiro(infoCalculo.getDib(), infoCalculo.getDataFim());
}


    private List<String> gerarListaComDecimoTerceiro(LocalDate dib, LocalDate fimCalculo) {
        List<String> listaDeDatas = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Adiciona a data inicial no formato
        listaDeDatas.add(dib.format(formatter));

        LocalDate dataAtual = dib.withDayOfMonth(1); // Começa no dia 01 do mês da data inicial

        while (dataAtual.isBefore(fimCalculo) || dataAtual.isEqual(fimCalculo)) {
            // Adiciona a data normal
            listaDeDatas.add(dataAtual.format(formatter));

            // Verifica o "mês 13"
            if (dataAtual.getMonthValue() == 12) {
                LocalDate mes13 = dataAtual.plusMonths(1); // Pula para o "mês 13" (janeiro do próximo ano)
                listaDeDatas.add("01/13/" + dataAtual.getYear());
                dataAtual = mes13; // Atualiza para o mês de janeiro do próximo ano
            } else {
                // Pula para o próximo mês normalmente
                dataAtual = dataAtual.plusMonths(1);
            }
        }

        // Adiciona a data final no formato se não estiver na lista
        if (!listaDeDatas.contains(fimCalculo.format(formatter))) {
            listaDeDatas.add(fimCalculo.format(formatter));
        }

        return listaDeDatas;
    }

    private List<String> gerarListaSemDecimoTerceiro(LocalDate dib, LocalDate fimCalculo) {
        List<String> listaDeDatas = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Adicionar a data inicial formatada
        listaDeDatas.add(dib.format(formatter));

        // Pular para o próximo mês
        LocalDate proximaData = dib.plusMonths(1).withDayOfMonth(1);

        // Iterar até o mês anterior da data final
        while (proximaData.isBefore(fimCalculo)) {
            // Adiciona a data 01 de cada mês
            listaDeDatas.add(proximaData.format(formatter));
            proximaData = proximaData.plusMonths(1);
        }

        // Adiciona a data final formatada
        listaDeDatas.add(fimCalculo.format(formatter));

        return listaDeDatas;
    }


    private boolean isBeneficioSemDecimoTerceiro(CalculoRequestDTO infoCalculo){
        return infoCalculo.getBeneficio() == BeneficiosEnum.SEGURO_DEFESO ||
                infoCalculo.getBeneficio() == BeneficiosEnum.LOAS_DEFICIENTE ||
                infoCalculo.getBeneficio() == BeneficiosEnum.LOAS_IDOSO;
    }
}
