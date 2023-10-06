package br.com.magnasistema.apicoleta.validacoes.funcionarioequipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.magnasistema.apicoleta.dto.funcionarioequipe.FuncionarioEquipeDtoCadastro;
import br.com.magnasistema.apicoleta.entity.Funcionario;
import br.com.magnasistema.apicoleta.enums.TipoFuncao;
import br.com.magnasistema.apicoleta.repository.EquipeRepository;
import br.com.magnasistema.apicoleta.repository.FuncionarioRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Component
public class ValidarUnicoMotorista implements ValidadorFuncionarioEquipe {

	@Autowired
	private EquipeRepository equipeRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	public void validar(FuncionarioEquipeDtoCadastro dados) {
		
		var funcionario = funcionarioRepository.getReferenceById(dados.idFuncionario());

		if (TipoFuncao.MOTORISTA.equals(funcionario.getFuncao())) {
			
			var equipe = equipeRepository.getReferenceById(dados.idEquipe());

			for (Funcionario f : equipe.getFuncionarios())  {
				if (TipoFuncao.MOTORISTA.equals(f.getFuncao())) {
					throw new ValidacaoException("Já existe um motorista na equipe.");
				}
			}
		}

	}

}
