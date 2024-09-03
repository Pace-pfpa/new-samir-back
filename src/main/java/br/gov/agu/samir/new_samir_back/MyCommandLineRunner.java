package br.gov.agu.samir.new_samir_back;

import br.gov.agu.samir.new_samir_back.service.strategy.CalculoCorrecaoMonetaria;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MyCommandLineRunner implements CommandLineRunner {


    private CalculoCorrecaoMonetaria calculoCorrecaoMonetaria;

    public MyCommandLineRunner(CalculoCorrecaoMonetaria calculoCorrecaoMonetaria) {
        this.calculoCorrecaoMonetaria = calculoCorrecaoMonetaria;
    }

    @Override
    public void run(String... args) throws Exception {
        BigDecimal valorCorrecao = calculoCorrecaoMonetaria.calcularIndexadorCorrecaoMonetaria(11, 2021);
        System.out.println("Valor da correção: " + valorCorrecao);

    }
}
