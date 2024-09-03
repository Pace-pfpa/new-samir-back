package br.gov.agu.samir.new_samir_back;

import br.gov.agu.samir.new_samir_back.service.strategy.factory.CorrecaoMonetariaFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MyCommandLineRunner implements CommandLineRunner {


    private final CorrecaoMonetariaFactory factory;

    public MyCommandLineRunner(CorrecaoMonetariaFactory factory) {
        this.factory = factory;
    }

    @Override
    public void run(String... args) throws Exception {
        BigDecimal valorCorrecaoIPCAE = factory.getCalculo("IPCAEeSELIC").calcularIndexadorCorrecaoMonetaria(11, 2021);
        BigDecimal valorCorrecaoINPC = factory.getCalculo("INPCeSELIC").calcularIndexadorCorrecaoMonetaria(11, 2021);
        System.out.println("Valor da correção SELIC + INPC: " + valorCorrecaoINPC);
        System.out.println("Valor da correção SELIC + IPCAE: " + valorCorrecaoIPCAE);

    }
}
