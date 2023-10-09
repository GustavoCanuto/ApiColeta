package br.com.magnasistema.apicoleta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.magnasistema.apicoleta.dto.funcionario.FuncionarioDtoListagemEquipes;
import br.com.magnasistema.apicoleta.dto.funcionarioequipe.FuncionarioEquipeDtoCadastro;
import br.com.magnasistema.apicoleta.dto.funcionarioequipe.FuncionarioEquipeDtoDetalhar;
import br.com.magnasistema.apicoleta.entity.Equipe;
import br.com.magnasistema.apicoleta.entity.Funcionario;
import br.com.magnasistema.apicoleta.entity.FuncionarioEquipe;
import br.com.magnasistema.apicoleta.repository.FuncionarioEquipeRepository;
import br.com.magnasistema.apicoleta.service.buscador.BuscarEquipe;
import br.com.magnasistema.apicoleta.service.buscador.BuscarFuncionario;
import br.com.magnasistema.apicoleta.validacoes.funcionarioequipe.ValidadorFuncionarioEquipe;

@Service
public class FuncionarioEquipeService {

	@Autowired
	private FuncionarioEquipeRepository funcionarioEquipeRepository;


	@Autowired
	private BuscarEquipe getEquipe;

	@Autowired
	private BuscarFuncionario getFuncionario;

	@Autowired
	private List<ValidadorFuncionarioEquipe> validadores;

	public FuncionarioEquipeDtoDetalhar cadastrarFuncionarioEquipe(FuncionarioEquipeDtoCadastro dados) {

		Equipe equipe = getEquipe.buscar(dados.idEquipe());
		
		Funcionario funcionario = getFuncionario.buscar(dados.idFuncionario());

		validadores.forEach(v -> v.validar(dados));
		
		var funcionarioEquipe = new FuncionarioEquipe(funcionario, equipe);

		funcionarioEquipeRepository.save(funcionarioEquipe);

		return new FuncionarioEquipeDtoDetalhar(funcionarioEquipe);

	}

	public FuncionarioDtoListagemEquipes listarTodasEquipesDeUmFuncionario(long id) {

		Funcionario funcionario = getFuncionario.buscar(id);

		return new FuncionarioDtoListagemEquipes(funcionario);
	}

	public void deletaCadastro(Long id) {

		funcionarioEquipeRepository.deleteById(id);

	}

	

}
