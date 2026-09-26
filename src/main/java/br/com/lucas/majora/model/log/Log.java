package br.com.lucas.majora.model.log;

public class Log {
	private String command;
	private String log;
	private String context;

	public Log(String command, String log) {
		this.command = command;
		this.log = log;
	}
	
	public Log(String command, String log, String context) {
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

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
	
	public String getContext() {
		return context;
	}
	
	public void setContext(String context) {
		this.context = context;
	}
	
	@Override
	public String toString() {
		return String.format(
				"Comando: %s\n"
			  + "context: %s\n"
			  + "-".repeat(20)
			  + "\nlog: %s\n"
			  + "-".repeat(20),
			  command,
			  context,
			  log
		);
	}
}
