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

import br.com.magnasistema.apicoleta.dto.destino.DestinoDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.destino.DestinoDtoCadastro;
import br.com.magnasistema.apicoleta.dto.destino.DestinoDtoDetalhar;
import br.com.magnasistema.apicoleta.service.DestinoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("destino")
public class DestinoController {

	@Autowired
	private DestinoService destinoService;

	@PostMapping
	@Transactional
	public ResponseEntity<DestinoDtoDetalhar> cadastrar(@RequestBody @Valid DestinoDtoCadastro dados,
			UriComponentsBuilder uriBuilder) {

		var destino = destinoService.cadastrarDestino(dados);

		var uri = uriBuilder.path("/destino/{id}").buildAndExpand(destino.id()).toUri();

		return ResponseEntity.created(uri).body(destino);

	}
	
	@GetMapping
	public ResponseEntity<Page<DestinoDtoDetalhar>> listar(
			@PageableDefault(size = 10) Pageable paginacao) {

		return ResponseEntity.ok(destinoService.listarDestinos(paginacao));


	}

	@GetMapping("/{id}")
	public ResponseEntity<DestinoDtoDetalhar> detalhar(@PathVariable Long id) {

		return ResponseEntity.ok(destinoService.detalharDestino(id));

	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<DestinoDtoDetalhar> atualizar(@PathVariable Long id, @RequestBody @Valid DestinoDtoAtualizar dados) {

		return ResponseEntity.ok(destinoService.atualizarCadastro(dados, id));

	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Void> excluir(@PathVariable Long id) {

		destinoService.deletaCadastro(id);

		return ResponseEntity.noContent().build();
	}

}
