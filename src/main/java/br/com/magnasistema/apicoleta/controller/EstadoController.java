package br.com.magnasistema.apicoleta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.magnasistema.apicoleta.dto.estado.EstadoDtoListagem;
import br.com.magnasistema.apicoleta.service.EstadoService;

@RestController
@RequestMapping("estado")
public class EstadoController {

	@Autowired
	private EstadoService estadoService;
	

	@GetMapping
	public ResponseEntity<List<EstadoDtoListagem>> listar() {

		return ResponseEntity.ok(estadoService.listarEstados());
	}

	
}
