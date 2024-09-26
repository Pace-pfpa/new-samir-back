package br.gov.agu.samir.new_samir_back;


import br.gov.agu.samir.new_samir_back.enums.TipoCorrecaoMonetaria;
import br.gov.agu.samir.new_samir_back.service.factory.CorrecaoMonetariaFactory;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@AllArgsConstructor
public class MyCommandLineRunner implements CommandLineRunner {


    private final CorrecaoMonetariaFactory factory;




    @Override
    public void run(String... args) throws Exception {

        BigDecimal indexadorCorrecaoMonetaria = factory.getCalculo(TipoCorrecaoMonetaria.TIPO4).calcularIndexadorCorrecaoMonetaria("01/01/2021");
        System.out.println("Indexador Correção Monetária: " + indexadorCorrecaoMonetaria);

    }
}
