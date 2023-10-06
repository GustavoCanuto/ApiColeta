package br.com.magnasistema.apicoleta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.magnasistema.apicoleta.dto.funcionario.FuncionarioDtoListagemEquipes;
import br.com.magnasistema.apicoleta.dto.funcionarioequipe.FuncionarioEquipeDtoCadastro;
import br.com.magnasistema.apicoleta.dto.funcionarioequipe.FuncionarioEquipeDtoDetalhar;
import br.com.magnasistema.apicoleta.service.FuncionarioEquipeService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("funcionarioequipe")
public class FuncionarioEquipeController {

	@Autowired
	private FuncionarioEquipeService funcionarioEquipeService;

	@PostMapping
	@Transactional
	public ResponseEntity<FuncionarioEquipeDtoDetalhar> cadastrar(@RequestBody @Valid FuncionarioEquipeDtoCadastro dados, UriComponentsBuilder uriBuilder) {
		
		var funcionarioEquipe =  funcionarioEquipeService.cadastrarFuncionarioEquipe(dados);

		var uri = uriBuilder.path("/funcionario/{id}").buildAndExpand(funcionarioEquipe.id()).toUri();

		return ResponseEntity.created(uri).body(funcionarioEquipe);  

	}
	
	@GetMapping("/funcionario/{id}")
	public ResponseEntity<FuncionarioDtoListagemEquipes> detalharComEquipes(@PathVariable long id,
			@PageableDefault(size = 10) Pageable paginacao
			) {

		return ResponseEntity.ok(funcionarioEquipeService.listarTodasEquipesDeUmFuncionario(id));

	}
	
	@DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
       
		funcionarioEquipeService.deletaCadastro(id);
    	
        return ResponseEntity.noContent().build();
    }

	
}
