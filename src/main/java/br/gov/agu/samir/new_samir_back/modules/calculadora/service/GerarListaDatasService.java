package br.gov.agu.samir.new_samir_back.modules.calculadora.service;

import br.gov.agu.samir.new_samir_back.modules.beneficio.enums.BeneficiosEnum;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Component
public class GerarListaDatasService {

    private static final Set<BeneficiosEnum> BENEFICIOS_SEM_DECIMO_TERCEIRO = EnumSet.of(
            BeneficiosEnum.PENSAO_MENSAL_VITALICIA_DO_SERINGUEIRO,
            BeneficiosEnum.PENSAO_MENSAL_VITALICIA_DO_DEPENDENTE_DO_SERINGUEIRO,
            BeneficiosEnum.LOAS_DEFICIENTE,
            BeneficiosEnum.LOAS_IDOSO,
            BeneficiosEnum.PENSAO_ESPECIAL_AO_PORTADOR_DE_HANSENIASE,
            BeneficiosEnum.SEGURO_DEFESO,
            BeneficiosEnum.SEGURO_DESEMPREGO
    );

    public List<String> gerarListaDatasPorBeneficioEperiodo(BeneficiosEnum beneficio,boolean decimoTerceiroFinalCalculo, LocalDate inicioCalculo ,LocalDate fimCalculo) {
        return isBeneficioSemDecimoTerceiro(beneficio) ?
                gerarListaSemDecimoTerceiro(inicioCalculo, fimCalculo) :
                gerarListaComDecimoTerceiro(inicioCalculo, fimCalculo, decimoTerceiroFinalCalculo);
    }




    private static List<String> gerarListaComDecimoTerceiro(LocalDate dataIncicio, LocalDate dataFim, boolean decioTerceiroNoFinal){
        List<String> datas = new ArrayList<>();
        LocalDate data = dataIncicio;
        datas.add(data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        if (YearMonth.from(data).equals(YearMonth.from(dataFim))) {
            if (decioTerceiroNoFinal) {
                String dataDecimoTerceiro = "01/13/" + data.getYear();
                datas.add(dataDecimoTerceiro);
            }

            return datas;
        }

        while (data.isBefore(dataFim)) {
            data = data.plusMonths(1).withDayOfMonth(1);
            if (isDataFinal(data, dataFim)) {
                datas.add(dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                if (decioTerceiroNoFinal) {
                    String dataDecimoTerceiro = "01/13/" + dataFim.getYear();
                    datas.add(dataDecimoTerceiro);
                }

                return datas;
            }
            datas.add(data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            if (data.getMonthValue() == 12){
                String dataDecimoTerceiro = "01/13/" + data.getYear();
                datas.add(dataDecimoTerceiro);
            }
        }

        if (decioTerceiroNoFinal) {
            String dataDecimoTerceiro = "01/13/" + dataFim.getYear();
            datas.add(dataDecimoTerceiro);
        }
        return datas;
    }

    private static List<String> gerarListaSemDecimoTerceiro(LocalDate dataInicio, LocalDate dataFim) {
        List<String> datas = new ArrayList<>();
        LocalDate data = dataInicio;
        datas.add(data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        if (YearMonth.from(data).equals(YearMonth.from(dataFim))) {
            return datas;
        }

        while (data.isBefore(dataFim)) {
            data = data.plusMonths(1).withDayOfMonth(1);
            if (isDataFinal(data, dataFim)) {
                datas.add(dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                return datas;
            }
            datas.add(data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        return datas;
    }

    private static boolean isDataFinal(LocalDate data, LocalDate dataFim) {
        return YearMonth.from(data).equals(YearMonth.from(dataFim));
    }



    private boolean isBeneficioSemDecimoTerceiro(BeneficiosEnum beneficio) {
        return BENEFICIOS_SEM_DECIMO_TERCEIRO.contains(beneficio);
    }
}
