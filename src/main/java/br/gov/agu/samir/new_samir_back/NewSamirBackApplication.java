package br.gov.agu.samir.new_samir_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NewSamirBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewSamirBackApplication.class, args);
	}

}
