package br.com.bandtec.tellusspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TellusApplication {

	public static void main(String[] args) {
		SpringApplication.run(TellusApplication.class, args);
	}

}
