package imoveis.aluguel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class AluguelApplication {

	public static void main(String[] args) {

		SpringApplication.run(AluguelApplication.class, args);

	}

}