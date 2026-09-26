package br.com.lucas.majora;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.lucas.majora.service.MajoraService;

@SpringBootApplication
public class MajoraApplication implements CommandLineRunner{
	private MajoraService majora;
	
	public MajoraApplication(MajoraService majora) {
		this.majora = majora;
	}

	public static void main(String[] args) {
		SpringApplication.run(MajoraApplication.class, args);
		
	}
	
	@Override
	public void run(String... args) {
		while(true) {
			
			majora.SendVoice();
		}
	}

}
