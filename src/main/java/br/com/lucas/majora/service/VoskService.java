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
	
	private static String extrairTextoPuro(String json, String chave) {

		String busca = "\"" + chave + "\" : \"";

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
		
		// Caminho do modelo
		String caminhoModelo = "/home/lucas/Documentos/vosk-model-pt-fb-v0.1.1-20220516_2113";

		// Configuração do áudio
		AudioFormat formatoAudio = new AudioFormat(16000, 16, 1, true, false);

		DataLine.Info info = new DataLine.Info(TargetDataLine.class, formatoAudio);

		// Verifica suporte ao microfone
		if (!AudioSystem.isLineSupported(info)) {

			System.err.println("Microfone não suportado com as configurações de áudio padrão.");

			System.exit(1);
		}

		// Palavra que ativa o assistente
		String palavraAtivacao = "computador";

		// Estado do assistente
		boolean assistenteAtivo = false;

		/*
		 * Vocabulário permitido pela grammar.
		 *
		 * OBS: "computador" precisa estar aqui porque é a palavra que ativa o
		 * assistente.
		 */
		String grammar = """
				    [
				        "computador",
				        "bash",
				        "src",
				        "main",
				        "home",
				        "maven",
				        "git",
				        "github",
				        "guit",
				        "majora",
				        "[unk]"
				    ]
				""";

		Thread loading = cli.loading();
		loading.start();
		
		// Inicializa Vosk e microfone
		try (Model modelo = new Model(caminhoModelo);
				
			Recognizer reconhecedor = new Recognizer(modelo, 16000, grammar);

			TargetDataLine linhaMicrofone = (TargetDataLine) AudioSystem.getLine(info)
			) {
			
			loading.interrupt();
			loading.join();
			
			// Inicia captura do microfone
			linhaMicrofone.open(formatoAudio);
			linhaMicrofone.start();

			System.out.println("\n>>> Sistema Iniciado! Diga '" + palavraAtivacao + "' para começar...");

			// Buffer de áudio
			byte[] buffer = new byte[4096];

			int bytesLidos;

			// Captura continuamente o áudio
			while ((bytesLidos = linhaMicrofone.read(buffer, 0, buffer.length)) >= 0) {

				/*
				 * O Vosk retorna true quando considera que terminou um trecho de fala.
				 */
				if (reconhecedor.acceptWaveForm(buffer, bytesLidos)) {

					String resultadoJson = reconhecedor.getResult();

					String textoFinal = extrairTextoPuro(resultadoJson, "text");

					if (textoFinal.isEmpty()) {
						continue;
					}

					textoFinal = textoFinal.toLowerCase().trim();

					/*
					 * ================================================== ASSISTENTE INATIVO
					 * ==================================================
					 *
					 * Aqui procuramos pela palavra "computador".
					 */

					if (!assistenteAtivo) {

						if (textoFinal.contains(palavraAtivacao)) {

							// Encontramos a palavra de ativação
							assistenteAtivo = true;

							System.out.println("\n[SINAL] " + palavraAtivacao.toUpperCase() + " detectado!");

							/*
							 * Descobre se o usuário falou um comando junto com a palavra de ativação.
							 *
							 * Exemplo:
							 *
							 * "computador abra o terminal"
							 *
							 * Resultado:
							 *
							 * "abra o terminal"
							 */

							int posicao = textoFinal.indexOf(palavraAtivacao);

							String comando = textoFinal.substring(posicao + palavraAtivacao.length()).trim();

							/*
							 * ================================================== PALAVRA-CHAVE + COMANDO
							 * ==================================================
							 */

							if (!comando.isEmpty()) {

								System.out.println("[COMANDO RECONHECIDO]: " + comando);

								System.out.println(">>> Enviando '" + comando + "' para processamento local...");

								/*
								 * O comando terminou.
								 *
								 * Volta para o estado inativo.
								 */
								assistenteAtivo = false;

								System.out.println(
										"\n>>> Processando requisição");
								
								majoraService.SendVoice(comando);
							}

							/*
							 * ================================================== APENAS PALAVRA-CHAVE
							 * ==================================================
							 */

							else {

								System.out.println(">>> Ouvindo o seu comando...");
							}
						}
					}

					/*
					 * ================================================== ASSISTENTE ATIVO
					 * ==================================================
					 *
					 * Aqui significa que o usuário falou:
					 *
					 * "computador"
					 *
					 * e agora está falando o comando.
					 */

					else {

						System.out.println("\n[COMANDO RECONHECIDO]: " + textoFinal);

						/*
						 * Comando especial para encerrar completamente o programa.
						 */
						if (textoFinal.equalsIgnoreCase("fechar assistente")) {

							System.out.println(">>> Desligando sistema de voz...");

							break;
						}

						/*
						 * Aqui futuramente entra o processamento do comando pelo Ollama.
						 */
						System.out.println(">>> Enviando '" + textoFinal + "' para processamento local...");

						/*
						 * O comando foi recebido.
						 *
						 * Agora o assistente volta a ficar INATIVO.
						 */
						assistenteAtivo = false;

						System.out.println("\n>>> Aguardando nova ativação... " + "Diga '" + palavraAtivacao + "'.");
						
						majoraService.SendVoice(textoFinal);
					}
				}

				/*
				 * ========================================================== RESULTADO PARCIAL
				 * ==========================================================
				 *
				 * Enquanto o usuário fala, mostramos a hipótese atual do Vosk, mas não
				 * processamos o comando ainda.
				 */

				else {

					if (assistenteAtivo) {

						String parcialJson = reconhecedor.getPartialResult();

						String textoParcial = extrairTextoPuro(parcialJson, "partial");

						if (!textoParcial.isEmpty()) {

							System.out.print("\rOuvindo: " + textoParcial);
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
