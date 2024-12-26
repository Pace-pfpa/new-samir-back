package br.gov.agu.samir.new_samir_back.util;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;


public class DinheiroFormatador {



    public static String formatarParaReal(BigDecimal valor) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(new Locale("pt", "BR"));
        simbolos.setDecimalSeparator(',');
        simbolos.setGroupingSeparator('.');

        // Criando o formatador com o padrão de moeda brasileira
        DecimalFormat formato = new DecimalFormat("#,##0.00", simbolos);

        // Formatando o valor e retornando como String
        return "R$ " + formato.format(valor);
    }

    public static BigDecimal formatarParaBigDecimal(String valor)  {
        try{
            valor.replace("R$ ", "");
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols(new Locale("pt", "BR"));
            simbolos.setDecimalSeparator(',');
            simbolos.setGroupingSeparator('.');

            // Criando o formatador com o padrão de moeda brasileira
            DecimalFormat formato = new DecimalFormat("#,##0.00", simbolos);

            // Formatando o valor e retornando como String
            return new BigDecimal(formato.parse(valor).toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
