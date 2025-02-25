package br.gov.agu.samir.new_samir_back.util;


import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


@Component
public class DateUtils {

    public LocalDate mapStringToLocalDate(String dataString) {
        try {
            return LocalDate.parse(dataString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            try {
                dataString = dataString.replace("13", "12");
                return LocalDate.parse(dataString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Invalid date format: " + dataString, ex);
            }
        }


    }

    public String mapLocalDateToString(LocalDate data) {
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}