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

import br.com.magnasistema.apicoleta.dto.veiculo.VeiculoDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.veiculo.VeiculoDtoCadastro;
import br.com.magnasistema.apicoleta.dto.veiculo.VeiculoDtoDetalhar;
import br.com.magnasistema.apicoleta.enums.TipoVeiculo;
import br.com.magnasistema.apicoleta.service.VeiculoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("veiculo")
public class VeiculoController {

	@Autowired
	private VeiculoService veiculoService;

	@PostMapping
	@Transactional
	public ResponseEntity<VeiculoDtoDetalhar> cadastrar(@RequestBody @Valid VeiculoDtoCadastro dados,
			UriComponentsBuilder uriBuilder) {

		var veiculo = veiculoService.cadastrarVeiculo(dados);

		var uri = uriBuilder.path("/veiculo/{id}").buildAndExpand(veiculo.id()).toUri();

		return ResponseEntity.created(uri).body(veiculo);

	}

	@GetMapping
	public ResponseEntity<Page<VeiculoDtoDetalhar>> listar(@PageableDefault(size = 10) Pageable paginacao,
			@RequestParam(name = "capacidade", required = false) Double capacidade,
			@RequestParam(name = "tipo", required = false) TipoVeiculo tipo) {

		return ResponseEntity.ok(veiculoService.listarVeiculos(paginacao, capacidade, tipo));

	}

	@GetMapping("/empresa/{idEmpresa}")
	public ResponseEntity<Page<VeiculoDtoDetalhar>> listar(@PathVariable long idEmpresa,
			@PageableDefault(size = 10) Pageable paginacao,
			@RequestParam(name = "capacidade", required = false) Double capacidade,
			@RequestParam(name = "tipo", required = false) TipoVeiculo tipo) {

		return ResponseEntity.ok(veiculoService.listarVeiculosPorEmpresa(paginacao, capacidade, tipo, idEmpresa));

	}

	@GetMapping("/{id}")
	public ResponseEntity<VeiculoDtoDetalhar> detalhar(@PathVariable Long id) {

		return ResponseEntity.ok(veiculoService.detalharVeiculo(id));
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<VeiculoDtoDetalhar> atualizar(@PathVariable Long id,
			@RequestBody @Valid VeiculoDtoAtualizar dados) {

		return ResponseEntity.ok(veiculoService.atualizarCadastro(dados, id));

	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Void> excluir(@PathVariable Long id) {

		veiculoService.deletaCadastro(id);

		return ResponseEntity.noContent().build();
	}

}
