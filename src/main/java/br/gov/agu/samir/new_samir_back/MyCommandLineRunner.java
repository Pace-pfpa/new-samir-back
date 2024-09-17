package br.gov.agu.samir.new_samir_back;


import br.gov.agu.samir.new_samir_back.dtos.CalculoRequestDTO;
import br.gov.agu.samir.new_samir_back.enums.TipoCorrecaoMonetaria;
import br.gov.agu.samir.new_samir_back.enums.TipoJuros;
import br.gov.agu.samir.new_samir_back.service.factory.CorrecaoMonetariaFactory;
import br.gov.agu.samir.new_samir_back.service.strategy.IndiceReajusteStrategy;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class MyCommandLineRunner implements CommandLineRunner {


    private final CorrecaoMonetariaFactory factory;

    private final List<IndiceReajusteStrategy> strategyList;



    @Override
    public void run(String... args) throws Exception {

        BigDecimal indexadorCorrecaoMonetaria = factory.getCalculo(TipoCorrecaoMonetaria.TIPO4).calcularIndexadorCorrecaoMonetaria("01/01/2021");
        System.out.println("Indexador Correção Monetária: " + indexadorCorrecaoMonetaria);

        CalculoRequestDTO requestDTO = CalculoRequestDTO.builder()
                .dib(LocalDate.of(2022, 12, 20))
                .dataFim(LocalDate.of(2023, 1, 1))
                .rmi(BigDecimal.valueOf(1000))
                .tipoJuros(TipoJuros.TIPO2)
                .tipoCorrecao(TipoCorrecaoMonetaria.TIPO4)
                .build();

        BigDecimal reajuste = strategyList.stream().map(impl -> impl.calcularIndiceReajuste(requestDTO, "01/01/2023")).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Reajuste: " + reajuste);
    }
}
