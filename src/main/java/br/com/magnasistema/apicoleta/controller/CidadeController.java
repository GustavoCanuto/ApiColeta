package br.com.magnasistema.apicoleta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.magnasistema.apicoleta.dto.cidade.CidadeDtoDetalhar;
import br.com.magnasistema.apicoleta.service.CidadeService;

@RestController
@RequestMapping("cidade")
public class CidadeController { 

	@Autowired
	private CidadeService cidadeService;

	@GetMapping
	public ResponseEntity<Page<CidadeDtoDetalhar>> listar(@RequestParam(name = "nome", required = false) String nome,
			@PageableDefault(size = 10) Pageable paginacao) {

		return ResponseEntity.ok(cidadeService.listarCidades(paginacao, nome));

	}

	@GetMapping("/estado/{id}")
	public ResponseEntity<Page<CidadeDtoDetalhar>> listarPorEstado(@PathVariable Long id,
			@RequestParam(name = "nome", required = false) String nome,
			@PageableDefault(size = 10) Pageable paginacao) {

		return ResponseEntity.ok(cidadeService.listarCidadesPorEstado(id, nome, paginacao));

	}

	@GetMapping("/{id}")
	public ResponseEntity<CidadeDtoDetalhar> detalhar(@PathVariable Long id) {

		return ResponseEntity.ok(cidadeService.detalharCidade(id));
	}

}
