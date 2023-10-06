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

import br.com.magnasistema.apicoleta.dto.funcionario.FuncionarioDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.funcionario.FuncionarioDtoCadastro;
import br.com.magnasistema.apicoleta.dto.funcionario.FuncionarioDtoDetalhar;
import br.com.magnasistema.apicoleta.enums.TipoFuncao;
import br.com.magnasistema.apicoleta.service.FuncionarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("funcionario")
public class FuncionarioController {

	@Autowired
	private FuncionarioService funcionarioService;

	@PostMapping
	@Transactional
	public ResponseEntity<FuncionarioDtoDetalhar> cadastrar(@RequestBody @Valid FuncionarioDtoCadastro dados,
			UriComponentsBuilder uriBuilder) {

		var funcionario = funcionarioService.cadastrarFuncionario(dados);

		var uri = uriBuilder.path("/bairro/{id}").buildAndExpand(funcionario.id()).toUri();

		return ResponseEntity.created(uri).body(funcionario);

	}

	@GetMapping
	public ResponseEntity<Page<FuncionarioDtoDetalhar>> listar(@PageableDefault(size = 10) Pageable paginacao,
			@RequestParam(name = "funcao", required = false) TipoFuncao funcao) {

		return ResponseEntity.ok(funcionarioService.listarFuncionarios(paginacao, funcao));

	}

	@GetMapping("/empresa/{id}")
	public ResponseEntity<Page<FuncionarioDtoDetalhar>> listar(@PathVariable long id,
			@PageableDefault(size = 10) Pageable paginacao,
			@RequestParam(name = "funcao", required = false) TipoFuncao funcao) {

		return ResponseEntity.ok(funcionarioService.listarFuncionariosPorEmpresa(paginacao, funcao, id));

	}

	@GetMapping("/{id}")
	public ResponseEntity<FuncionarioDtoDetalhar> detalhar(@PathVariable Long id) {

		return ResponseEntity.ok(funcionarioService.detalharFuncionario(id));
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<FuncionarioDtoDetalhar> atualizar(@PathVariable Long id,
			@RequestBody @Valid FuncionarioDtoAtualizar dados) {

		return ResponseEntity.ok(funcionarioService.atualizarCadastro(dados, id));
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Void> excluir(@PathVariable Long id) {

		funcionarioService.deletaCadastro(id);

		return ResponseEntity.noContent().build();
	}

}
