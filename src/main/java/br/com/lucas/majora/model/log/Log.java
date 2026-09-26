package br.com.lucas.majora.model.log;

import java.util.List;

public class Log {
	private String command;
	private List<String> log;
	private String context;

	public Log(String command, List<String> log) {
		this.command = command;
		this.log = log;
	}
	
	public Log(String command, List<String> log, String context) {
		this.command = command;
		this.log = log;
		this.context = context;
	}
	
	public Log(String context) {
		this.context = context;
	}
	
	public Log() {
		
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<String> getLog() {
		return log;
	}

	public void setLog(List<String >log) {
		this.log = log;
	}
	
	public String getContext() {
		return context;
	}
	
	public void setContext(String context) {
		this.context = context;
	}
}
