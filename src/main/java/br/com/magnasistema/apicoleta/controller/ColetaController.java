package br.com.magnasistema.apicoleta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoCadastro;
import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoDetalhar;
import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoModificar;
import br.com.magnasistema.apicoleta.service.ColetaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("coleta") 
public class ColetaController {
	
	@Autowired
	private ColetaService coletaService;

	@PostMapping
	@Transactional
	public ResponseEntity<ColetaDtoDetalhar> cadastrar(@RequestBody @Valid ColetaDtoCadastro dados,
			UriComponentsBuilder uriBuilder) {

		var coleta = coletaService.cadastrarColeta(dados);

		var uri = uriBuilder.path("/coleta/{id}").buildAndExpand(coleta.id()).toUri();

		return ResponseEntity.created(uri).body(coleta);

	}
	
	@GetMapping
	public ResponseEntity<Page<ColetaDtoDetalhar>> listar(
			@PageableDefault(size = 10) Pageable paginacao) {

		return ResponseEntity.ok(coletaService.listarColetas(paginacao));


	}
	
	@GetMapping("/equipe/{id}")
	public ResponseEntity<Page<ColetaDtoDetalhar>> listarPorEquipe(
			@PathVariable Long id,
			@PageableDefault(size = 10) Pageable paginacao) {

		return ResponseEntity.ok(coletaService.listarColetaPorEquipe(paginacao, id));

	}
	
	

	@GetMapping("/{id}")
	public ResponseEntity<ColetaDtoDetalhar> detalhar(@PathVariable Long id) {

		return ResponseEntity.ok(coletaService.detalharColeta(id));

	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<ColetaDtoDetalhar> atualizar(@PathVariable Long id, @RequestBody @Valid ColetaDtoAtualizar dados) {

		return ResponseEntity.ok(coletaService.atualizarCadastro(dados, id));

	}
	
	@PutMapping("modificar/{id}")
	@Transactional
	public ResponseEntity<ColetaDtoDetalhar> modificar(@PathVariable Long id, @RequestBody @Valid ColetaDtoModificar dados) {

		return ResponseEntity.ok(coletaService.modificarColeta(dados, id)); 

	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Void> excluir(@PathVariable Long id) {

		coletaService.deletaCadastro(id);

		return ResponseEntity.noContent().build();
	}

}
