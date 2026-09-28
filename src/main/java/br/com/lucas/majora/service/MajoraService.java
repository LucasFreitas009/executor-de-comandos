package br.com.lucas.majora.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.lucas.majora.model.log.Log;
import br.com.lucas.majora.model.request.MajoraRequest;
import br.com.lucas.majora.model.response.MajoraResponse;
import br.com.lucas.majora.view.MajoraCLI;

@Service
public class MajoraService {
	private final RestTemplate rest;
	private CommandService command;
	private MajoraCLI cli = new MajoraCLI();
	
	public MajoraService(RestTemplate rest, CommandService command) {
		this.rest = rest;
		this.command = command;
	}
	
	public ResponseEntity<Log> sendJson(MajoraRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<MajoraRequest> entity = new HttpEntity<>(request, headers);
		
		ResponseEntity<MajoraResponse> response = rest.postForEntity("http://192.168.10.23:11434/api/chat", entity, MajoraResponse.class);
		
		String instructionJson = response.getBody().getMessage().getContent();
		
		return ResponseEntity.ok(command.executeCommand(instructionJson));
	}
	
	public void SendVoice(String content) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		MajoraRequest request = new MajoraRequest();
		
		MajoraRequest.Messages message = new MajoraRequest.Messages();
		message.setContent(content);
		
		List<MajoraRequest.Messages> messages = new ArrayList<>();
		request.setMessages(messages);
		request.getMessages().add(message);
		
		HttpEntity<MajoraRequest> entity = new HttpEntity<>(request, headers);
		
		ResponseEntity<MajoraResponse> response = rest.postForEntity("http://192.168.10.23:11434/api/chat", entity, MajoraResponse.class);
		
		String instruction = response.getBody().getMessage().getContent();
		
		cli.logView(command.executeCommand(instruction));
	}
	
}
