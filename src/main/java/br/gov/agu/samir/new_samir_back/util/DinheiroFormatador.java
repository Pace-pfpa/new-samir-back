package br.gov.agu.samir.new_samir_back.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DinheiroFormatador {



    public String formatarParaReal(BigDecimal valor) {
        return "R$ " + valor.toString();
    }

    public BigDecimal formatarParaBigDecimal(String valor) {
        return new BigDecimal(valor.replace("R$ ", ""));
    }
}
