package br.gov.agu.samir.new_samir_back.util;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateUtils {

    public static List<String> gerarListaDataComDecimoTerceiro(LocalDate startDate, LocalDate endDate) {
        List<String> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate.format(formatter));
            if (currentDate.getMonthValue() == 12) {
                LocalDate month13 = currentDate.plusMonths(1);
                dates.add("01/13/" + (currentDate.getYear()));
                currentDate = month13.withDayOfMonth(1);
            } else {
                currentDate = currentDate.plusMonths(1).withDayOfMonth(1);
            }
        }
        return dates;
    }
    public static List<String> gerarListaDeDatasSemDecimoTerceiro(LocalDate startDate, LocalDate endDate) {
        List<String> formattedDates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Adiciona a data inicial formatada
        formattedDates.add(startDate.format(formatter));

        // Itera de mês em mês até a data final
        LocalDate tempDate = startDate.withDayOfMonth(1).plusMonths(1);
        while (tempDate.isBefore(endDate) || tempDate.isEqual(endDate)) {
            formattedDates.add(tempDate.format(formatter));
            tempDate = tempDate.plusMonths(1);
        }

        // Adiciona a data final formatada, se necessário
        if (!formattedDates.contains(endDate.format(formatter))) {
            formattedDates.add(endDate.format(formatter));
        }

        return formattedDates;
    }


}