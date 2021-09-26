package br.com.bandtec.tellusspringboot;

import br.com.bandtec.tellusspringboot.utils.ListaObjeto;
import br.com.bandtec.tellusspringboot.utils.Requisicao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TellusApplication {
	public static ListaObjeto<Requisicao> listaReqTratadas = new ListaObjeto<>(20);

	public static void main(String[] args) {
		SpringApplication.run(TellusApplication.class, args);
	}

}
