package br.com.lucas.majora.model.request.instruction;

import java.util.List;

public class CommandInstruction {

	private List<String> command;
	private String context;

	public List<String> getCommand() {
		return command;
	}

	public void setCommand(List<String> command) {
		this.command = command;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
}
