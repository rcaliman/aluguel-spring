package imoveis.aluguel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class AluguelApplication {

	public static void main(String[] args) {

		SpringApplication.run(AluguelApplication.class, args);

	}

}