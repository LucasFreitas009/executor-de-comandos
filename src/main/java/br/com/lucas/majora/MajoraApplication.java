package br.com.lucas.majora;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.lucas.majora.service.VoskService;

@SpringBootApplication
public class MajoraApplication implements CommandLineRunner{
	private VoskService vosk;
	
	public MajoraApplication(VoskService vosk) {
		this.vosk = vosk;
	}

	public static void main(String[] args) {
		SpringApplication.run(MajoraApplication.class, args);
		
	}
	
	@Override
	public void run(String... args) {
		vosk.voice();
	}

}
