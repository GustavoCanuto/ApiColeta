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

import br.com.magnasistema.apicoleta.dto.empresa.EmpresaDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.empresa.EmpresaDtoCadastrar;
import br.com.magnasistema.apicoleta.dto.empresa.EmpresaDtoDetalhar;
import br.com.magnasistema.apicoleta.service.EmpresaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("empresa")
public class EmpresaController {

	@Autowired
	private EmpresaService empresaService;

	@PostMapping
	@Transactional
	public ResponseEntity<EmpresaDtoDetalhar> cadastrar(@RequestBody @Valid EmpresaDtoCadastrar dados,
			UriComponentsBuilder uriBuilder) {

		var empresa = empresaService.cadastrarEmpresa(dados);

		var uri = uriBuilder.path("/empresa/{id}").buildAndExpand(empresa.id()).toUri();

		return ResponseEntity.created(uri).body(empresa);

	}

	@GetMapping
	public ResponseEntity<Page<EmpresaDtoDetalhar>> listar(@PageableDefault(size = 10) Pageable paginacao,
			@RequestParam(name = "nome", required = false) String nome) {

		return ResponseEntity.ok(empresaService.listarEmpresas(paginacao, nome));

	}

	@GetMapping("/{id}")
	public ResponseEntity<EmpresaDtoDetalhar> detalhar(@PathVariable Long id) {

		return ResponseEntity.ok(empresaService.detalharEmpresa(id));
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<EmpresaDtoDetalhar> atualizar(@PathVariable Long id, @RequestBody @Valid EmpresaDtoAtualizar dados) {

		return ResponseEntity.ok(empresaService.atualizarCadastro(dados, id));

	
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<EmpresaDtoDetalhar> excluir(@PathVariable Long id) {

		empresaService.deletaCadastro(id);

		return ResponseEntity.noContent().build();
	}

}
