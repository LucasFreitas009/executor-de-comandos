package br.com.lucas.majora.model.request;

import java.util.List;

public class MajoraRequest {
	private String model = "majora:latest";
	private List<Messages> messages;
	private boolean think = false;
	private boolean stream = false;
	
	
	//Métodos da classe Requestbody
	public String getModel() {
		return model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public boolean getThink() {
		return think;
	}
	
	public void setThink(boolean think) {
		this.think = think;
	}
	
	public boolean getStream() {
		return stream;
	}
	
	public void setStream(boolean stream) {
		this.stream = stream;
	}
	
	
	//Métodos para resgatar dados da classe Messages
	public List<Messages> getMessages() {
		return messages;
	}
	
	public void setMessages(List<Messages> messages) {
		this.messages = messages;
	}
	
	
	public static class Messages {
		private String role = "user";
		private String content;
		
		public String getContent() {
			return content;
		}
		
		public void setContent(String content) {
			this.content = content;
		}
		
		public  String getRole() {
			return role;
		}
		
		public void setRole(String role) {
			this.role = role;
		}
	}
}
