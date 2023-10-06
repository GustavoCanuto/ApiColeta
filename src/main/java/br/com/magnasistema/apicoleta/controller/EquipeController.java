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

import br.com.magnasistema.apicoleta.dto.equipe.EquipeDtoCadastro;
import br.com.magnasistema.apicoleta.dto.equipe.EquipeDtoDetalhar;
import br.com.magnasistema.apicoleta.dto.equipe.EquipeDtoDetalharComFuncionarios;
import br.com.magnasistema.apicoleta.service.EquipeService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("equipe")
public class EquipeController {

	@Autowired
	private EquipeService equipeService;

	@PostMapping
	@Transactional
	public ResponseEntity<EquipeDtoDetalharComFuncionarios> cadastrar(@RequestBody @Valid EquipeDtoCadastro dados,
			UriComponentsBuilder uriBuilder) {

		var equipe = equipeService.cadastrarEquipe(dados);

		var uri = uriBuilder.path("/equipe/{id}").buildAndExpand(equipe.id()).toUri();

		return ResponseEntity.created(uri).body(equipe);

	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<EquipeDtoDetalharComFuncionarios> atualizar(@PathVariable Long id, @RequestBody EquipeDtoCadastro dados) {

		return ResponseEntity.ok(equipeService.atualizarEquipe(dados, id));

	}
	
	@GetMapping
	public ResponseEntity<Page<EquipeDtoDetalhar>> listar(@PageableDefault(size = 10) Pageable paginacao) {

		return ResponseEntity.ok(equipeService.listarEquipeSimples(paginacao));

	}

	@GetMapping("/{id}")
	public ResponseEntity<EquipeDtoDetalharComFuncionarios> detalhar(@PathVariable Long id) {

		return ResponseEntity.ok(equipeService.detalharEquipe(id));

	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Void> excluir(@PathVariable Long id) {

		equipeService.deletaCadastro(id);

		return ResponseEntity.noContent().build();
	}

}
