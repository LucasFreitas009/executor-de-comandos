package br.com.lucas.majora.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.lucas.majora.commands.Command;
import br.com.lucas.majora.model.log.Log;
import br.com.lucas.majora.model.request.MajoraRequest;
import br.com.lucas.majora.model.request.instruction.CommandInstruction;
import br.com.lucas.majora.model.response.MajoraResponse;
import tools.jackson.databind.ObjectMapper;

@Service
public class MajoraService {
	private final RestTemplate rest;
	private final ObjectMapper mapper;
	private final VoskService vosk;
	
	public MajoraService(RestTemplate rest, ObjectMapper mapper, VoskService vosk) {
		this.rest = rest;
		this.mapper = mapper;
		this.vosk = vosk;
	}
	
	public ResponseEntity<Log> sendJson(MajoraRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<MajoraRequest> entity = new HttpEntity<>(request, headers);
		
		ResponseEntity<MajoraResponse> response = rest.postForEntity("http://192.168.10.23:11434/api/chat", entity, MajoraResponse.class);
		
		String instructionJson = response.getBody().getMessage().getContent();
		
		CommandInstruction instruction = mapper.readValue(instructionJson, CommandInstruction.class);
		
		
		return ResponseEntity.ok(Command.execution(instruction));
	}
	
	public void SendVoice() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		MajoraRequest request = new MajoraRequest();
		
		MajoraRequest.Messages message = new MajoraRequest.Messages();
		message.setContent(vosk.voice());
		
		List<MajoraRequest.Messages> messages = new ArrayList<>();
		request.setMessages(messages);
		request.getMessages().add(message);
		
		HttpEntity<MajoraRequest> entity = new HttpEntity<>(request, headers);
		
		ResponseEntity<MajoraResponse> response = rest.postForEntity("http://192.168.10.23:11434/api/chat", entity, MajoraResponse.class);
		
		String instructionJson = response.getBody().getMessage().getContent();
		System.out.println(instructionJson);
		
		CommandInstruction instruction = mapper.readValue(instructionJson, CommandInstruction.class);
		
		Log log = Command.execution(instruction);
		
		System.out.println(log.toString());
	}
	
}
