package br.com.lucas.majora.service;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import org.springframework.stereotype.Service;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import br.com.lucas.majora.view.MajoraCLI;

@Service
public class VoskService {
	
	private MajoraCLI cli = new MajoraCLI();
	private final MajoraService majoraService;
	
	public VoskService(MajoraService majoraService) {
		this.majoraService = majoraService;
	}
	
	private static String textLog(String json, String key) {

		String busca = "\"" + key + "\" : \"";

		int inicio = json.indexOf(busca);

		if (inicio == -1) {
			return "";
		}

		inicio += busca.length();

		int fim = json.indexOf("\"", inicio);

		if (fim == -1) {
			return "";
		}

		return json.substring(inicio, fim).trim();
	}

	public void voice() {
		LibVosk.setLogLevel(LogLevel.WARNINGS);
				
		String pathModel = "/home/lucas/Documentos/vosk-model-pt-fb-v0.1.1-20220516_2113";

		AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);

		DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

		if (!AudioSystem.isLineSupported(info)) {

			System.err.println("Microfone não suportado com as configurações de áudio padrão.");

			System.exit(1);
		}

		String keyWord = "computador";

		boolean majoraActivated = false;

		Thread loading = cli.loading();
		loading.start();
		
		try (Model model = new Model(pathModel);
				
			Recognizer recognizer = new Recognizer(model, 16000);

			TargetDataLine microfone = (TargetDataLine) AudioSystem.getLine(info)
			) {
			
			loading.interrupt();
			loading.join();
			
			microfone.open(audioFormat);
			microfone.start();

			System.out.println("\n>>> Sistema Iniciado! Diga '" + keyWord + "' para começar...");

			byte[] buffer = new byte[4096];

			int bytesLidos;

			while ((bytesLidos = microfone.read(buffer, 0, buffer.length)) >= 0) {

				if (recognizer.acceptWaveForm(buffer, bytesLidos)) {

					String jsonResult = recognizer.getResult();

					String finalText = textLog(jsonResult, "text");

					if (finalText.isEmpty()) {
						continue;
					}

					finalText = finalText.toLowerCase().trim();

					if (!majoraActivated) {

						if (finalText.contains(keyWord)) {

							majoraActivated = true;

							System.out.println("\n[SINAL] " + keyWord.toUpperCase() + " detectado!");

							int position = finalText.indexOf(keyWord);

							String command = finalText.substring(position + keyWord.length()).trim();

							if (!command.isEmpty()) {

								System.out.println("[COMANDO RECONHECIDO]: " + command);

								System.out.println(">>> Enviando '" + command + "' para processamento local...");

								majoraActivated = false;

								System.out.println(
										"\n>>> Processando requisição");
								
								majoraService.SendVoice(command);
								
								System.out.println("Aguardando nova ativação...");
							}

							else {

								System.out.println(">>> Ouvindo o seu comando...");
							}
						}
					}

					else {

						System.out.println("\n[COMANDO RECONHECIDO]: " + finalText);

						if (finalText.equalsIgnoreCase("fechar assistente")) {

							System.out.println(">>> Desligando sistema de voz...");

							break;
						}

						System.out.println(">>> Enviando '" + finalText + "' para processamento local...");

						majoraActivated = false;

						System.out.println("\n>>> Processando requisição.");
						
						majoraService.SendVoice(finalText);
					}
					
				} else {

					if (majoraActivated) {

						String parcialJson = recognizer.getPartialResult();

						String parcialText = textLog(parcialJson, "partial");

						if (!parcialText.isEmpty()) {

							System.out.print("\rOuvindo: " + parcialText);
						}
					}
				}
			}

		} catch (Exception e) {

			System.err.println("Erro durante a execução: " + e.getMessage());

			e.printStackTrace();
		}
	}
}
