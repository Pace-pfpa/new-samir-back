package br.gov.agu.samir.new_samir_back;


import br.gov.agu.samir.new_samir_back.service.strategy.CalculoJuros;
import br.gov.agu.samir.new_samir_back.service.strategy.factory.CorrecaoMonetariaFactory;
import br.gov.agu.samir.new_samir_back.service.strategy.impl.JUROSeSELIC;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class MyCommandLineRunner implements CommandLineRunner {


    private final CorrecaoMonetariaFactory factory;

    private final CalculoJuros calculo;


    public MyCommandLineRunner(CorrecaoMonetariaFactory factory, JUROSeSELIC calculo) {
        this.factory = factory;
        this.calculo = calculo;
    }

    @Override
    public void run(String... args) throws Exception {
        BigDecimal valorCorrecaoIPCAE = factory.getCalculo("IPCAEeSELIC").calcularIndexadorCorrecaoMonetaria(LocalDate.of(2020,3,1));
        BigDecimal valorCorrecaoINPC = factory.getCalculo("INPCeSELIC").calcularIndexadorCorrecaoMonetaria(LocalDate.of(2020,1,1));
        BigDecimal valorJuros = calculo.calcularJuros(LocalDate.of(2020,7,1));
        System.out.println("Valor dos juros: " + valorJuros);
        System.out.println("Valor da correção SELIC + INPC: " + valorCorrecaoINPC);
        System.out.println("Valor da correção SELIC + IPCAE: " + valorCorrecaoIPCAE);

    }
}
