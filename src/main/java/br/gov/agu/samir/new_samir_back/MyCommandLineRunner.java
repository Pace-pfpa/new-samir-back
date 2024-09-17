package br.gov.agu.samir.new_samir_back;


import br.gov.agu.samir.new_samir_back.enums.TipoCorrecaoMonetaria;
import br.gov.agu.samir.new_samir_back.service.factory.CalculoJuros;
import br.gov.agu.samir.new_samir_back.service.factory.CorrecaoMonetariaFactory;
import br.gov.agu.samir.new_samir_back.service.factory.impl.JUROSeSELICimpl;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@AllArgsConstructor
public class MyCommandLineRunner implements CommandLineRunner {


    private final CorrecaoMonetariaFactory factory;

    private final CalculoJuros calculo;




    @Override
    public void run(String... args) throws Exception {

        BigDecimal indexadorCorrecaoMonetaria = factory.getCalculo(TipoCorrecaoMonetaria.TIPO4).calcularIndexadorCorrecaoMonetaria(LocalDate.of(2021, 2, 1));
        System.out.println("Indexador Correção Monetária: " + indexadorCorrecaoMonetaria);
    }
}
