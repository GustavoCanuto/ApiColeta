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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.magnasistema.apicoleta.dto.bairro.BairroDtoCadastro;
import br.com.magnasistema.apicoleta.dto.bairro.BairroDtoDetalhar;
import br.com.magnasistema.apicoleta.service.BairroService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("bairro")
public class BairroController {

	@Autowired
	private BairroService bairroService;

	@PostMapping
	@Transactional
	public ResponseEntity<BairroDtoDetalhar> cadastrar(@RequestBody @Valid BairroDtoCadastro dados,
			UriComponentsBuilder uriBuilder) {

		var bairro = bairroService.cadastrarBairro(dados);

		var uri = uriBuilder.path("/bairro/{id}").buildAndExpand(bairro.id()).toUri();

		return ResponseEntity.created(uri).body(bairro);

	}

	@GetMapping
	public ResponseEntity<Page<BairroDtoDetalhar>> listar(
			@PageableDefault(size = 10, sort = { "nome" }) Pageable paginacao,
			@RequestParam(name = "nome", required = false) String nome) {

		return ResponseEntity.ok(bairroService.listarBairros(paginacao, nome));

	}

	@GetMapping("/{id}")
	public ResponseEntity<BairroDtoDetalhar> detalhar(@PathVariable Long id) {

		return ResponseEntity.ok(bairroService.detalharBairro(id));
	}

	@GetMapping("/cidade/{id}")
	public ResponseEntity<Page<BairroDtoDetalhar>> listarPorCidade(@PathVariable Long id,
			@RequestParam(name = "nome", required = false) String nome,
			@PageableDefault(size = 10) Pageable paginacao) {

		return ResponseEntity.ok(bairroService.listarBairrosPorCidade(id, nome, paginacao));

	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<BairroDtoDetalhar> atualizar(@PathVariable Long id, @RequestBody BairroDtoCadastro dados) {

		return ResponseEntity.ok(bairroService.atualizarCadastro(dados, id));

	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Void> excluir(@PathVariable Long id) {

		bairroService.deletaCadastro(id);

		return ResponseEntity.noContent().build();
	}

}
