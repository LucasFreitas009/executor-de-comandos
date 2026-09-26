package br.com.lucas.majora.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.lucas.majora.model.log.Log;
import br.com.lucas.majora.model.request.instruction.CommandInstruction;

public class Command {
	
	public static Log execution(CommandInstruction instruction) {
		
		if(!instruction.getCommand().isEmpty()) {
			
			ProcessBuilder exec = new ProcessBuilder(instruction.getCommand());
			
			try {
				exec.redirectErrorStream(true);
				Process process = exec.start();
				
				BufferedReader reader = process.inputReader();
				String line;
				List<String> log = new ArrayList<>();
				
				while((line = reader.readLine()) != null) {
					log.add(line);
				}
				
				String command = String.join(" ", instruction.getCommand());
				List<String> logContent = log;
				String context = instruction.getContext();
				
				return new Log(command, logContent, context);
				
			} catch(IOException e) {
				return new Log("Empty", null, "Empty");
			}
			
		} else {
			return new Log(instruction.getContext());
		}
	}
}
