package br.com.lucas.majora.view;

import br.com.lucas.majora.model.log.Log;

public class MajoraCLI {
	
	public Thread loading() {
		
		Thread loading = new Thread(() -> {
			
			System.out.println(
		""" 
			╔════════════════════╗
			║                    ║
			║       MAJORA       ║
			║                    ║
			╚════════════════════╝		
							
		"""	
		);
			
		
			try {
				Thread.sleep(200);
				System.out.println("Iniciando Sistema.\n");
				
				while(!Thread.currentThread().isInterrupted()) {
					
					String[] spinner = {"\\", "|", "/", "-"};
					
					
					for(int i = 0; i < 10; i++) {
						System.out.print("\r[Aguardando sistema de voz.]  " + spinner[i % spinner.length]);
						Thread.sleep(500);
					}
				}
				System.out.println(" ");
			} catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});
		
		return loading;
	}
	
	public void logView(Log log) {
		
		System.out.println(" ");
		
		System.out.print(String.format("""
		    [LOG]║
		 ╔═══════╝
		 ║[Comando executado] -> {%s}
		 ║
		 ║[Majora]: \"%s\" 
		""", log.getCommand(), log.getContext()));
		System.out.println("");
		for(int i = 0; i < log.getLog().size(); i ++) {
			System.out.println(" ║" + log.getLog().get(i));
		}
		System.out.println("═".repeat(20));
	}
}
