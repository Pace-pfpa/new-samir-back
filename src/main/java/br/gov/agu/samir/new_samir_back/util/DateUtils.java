package br.gov.agu.samir.new_samir_back.util;


import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Component
public class DateUtils {

    public LocalDate mapStringToLocalDate(String dataString) {
        return LocalDate.parse(dataString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public LocalDate mapDecimoTerceiroToLocalDate(String dataString) {
        return LocalDate.parse(dataString.replace("13", "12"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

}