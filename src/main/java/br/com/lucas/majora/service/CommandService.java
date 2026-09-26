package br.com.lucas.majora.service;

import org.springframework.stereotype.Service;

import br.com.lucas.majora.commands.Command;
import br.com.lucas.majora.model.log.Log;
import br.com.lucas.majora.model.request.instruction.CommandInstruction;
import tools.jackson.databind.ObjectMapper;

@Service
public class CommandService {
	
	public final ObjectMapper mapper;
	
	public CommandService(ObjectMapper mapper) {
		this.mapper = mapper;
	}
	
	
	public Log executeCommand(String instruction) {
		CommandInstruction commandInstruction = mapper.readValue(instruction, CommandInstruction.class);
		
		Log log = Command.execution(commandInstruction);
		
		return log;
	}
}
