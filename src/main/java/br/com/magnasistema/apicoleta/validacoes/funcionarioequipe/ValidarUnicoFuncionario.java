package br.com.magnasistema.apicoleta.validacoes.funcionarioequipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.magnasistema.apicoleta.dto.funcionarioequipe.FuncionarioEquipeDtoCadastro;
import br.com.magnasistema.apicoleta.repository.FuncionarioEquipeRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Component
public class ValidarUnicoFuncionario implements ValidadorFuncionarioEquipe {

	@Autowired
	private FuncionarioEquipeRepository funcionarioEquipeRepository;

	public void validar(FuncionarioEquipeDtoCadastro dados) {

		if (funcionarioEquipeRepository.existsByFuncionarioIdAndEquipeId(dados.idFuncionario(), dados.idEquipe())) {
			throw new ValidacaoException("O funcionário já está na equipe.");
		}

	}
}
