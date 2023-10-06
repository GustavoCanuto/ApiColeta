package br.com.magnasistema.apicoleta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.magnasistema.apicoleta.dto.funcionario.FuncionarioDtoListagemEquipes;
import br.com.magnasistema.apicoleta.dto.funcionarioequipe.FuncionarioEquipeDtoCadastro;
import br.com.magnasistema.apicoleta.dto.funcionarioequipe.FuncionarioEquipeDtoDetalhar;
import br.com.magnasistema.apicoleta.entity.Funcionario;
import br.com.magnasistema.apicoleta.entity.FuncionarioEquipe;
import br.com.magnasistema.apicoleta.repository.EquipeRepository;
import br.com.magnasistema.apicoleta.repository.FuncionarioEquipeRepository;
import br.com.magnasistema.apicoleta.repository.FuncionarioRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;
import br.com.magnasistema.apicoleta.validacoes.funcionarioequipe.ValidadorFuncionarioEquipe;

@Service
public class FuncionarioEquipeService {

	@Autowired
	private FuncionarioEquipeRepository funcionarioEquipeRepository;

	@Autowired
	private EquipeRepository equipeRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private List<ValidadorFuncionarioEquipe> validadores;

	public FuncionarioEquipeDtoDetalhar cadastrarFuncionarioEquipe(FuncionarioEquipeDtoCadastro dados) {

		var equipe = equipeRepository.findById(dados.idEquipe())
				.orElseThrow(() -> new ValidacaoException("Id da Equipe não encontrado"));

		var funcionario = funcionarioRepository.findById(dados.idFuncionario())
				.orElseThrow(() -> new ValidacaoException("Id do Funcionario não encontrado")); 


		validadores.forEach(v -> v.validar(dados));
		
		var funcionarioEquipe = new FuncionarioEquipe(funcionario, equipe);

		funcionarioEquipeRepository.save(funcionarioEquipe);

		return new FuncionarioEquipeDtoDetalhar(funcionarioEquipe);

	}

	public FuncionarioDtoListagemEquipes listarTodasEquipesDeUmFuncionario(long id) {

		Funcionario funcionario = funcionarioRepository.getReferenceById(id);

		return new FuncionarioDtoListagemEquipes(funcionario);
	}

	public void deletaCadastro(Long id) {

		funcionarioEquipeRepository.deleteById(id);

	}

	

}
