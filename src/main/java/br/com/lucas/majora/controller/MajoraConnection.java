package br.com.lucas.majora.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lucas.majora.model.request.MajoraRequest;
import br.com.lucas.majora.service.MajoraService;

@RestController
@RequestMapping("/majora")
public class MajoraConnection {
	
	private final MajoraService service;
	
	public MajoraConnection(MajoraService service) {
		this.service = service;
	}
	
	@PostMapping("/send")
	public ResponseEntity<?> request(@RequestBody MajoraRequest body) {
		return service.sendJson(body);
	}
}
