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
public class ValidarLimiteColetores implements ValidadorFuncionarioEquipe {

	@Autowired
	private EquipeRepository equipeRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	public void validar(FuncionarioEquipeDtoCadastro dados) {

		int contadorColetores = 0;

		var funcionario = funcionarioRepository.getReferenceById(dados.idFuncionario());

		if (TipoFuncao.COLETOR.equals(funcionario.getFuncao())
				
				|| TipoFuncao.COLETOR_AUXILIAR.equals(funcionario.getFuncao())) {

			var equipe = equipeRepository.getReferenceById(dados.idEquipe());

			for (Funcionario f : equipe.getFuncionarios()) {

				if (TipoFuncao.COLETOR.equals(f.getFuncao()) || TipoFuncao.COLETOR_AUXILIAR.equals(f.getFuncao())) {

					contadorColetores++;
				}
			}

			if (contadorColetores == 4) {
				throw new ValidacaoException("A equipe já tem 4 coletores");
			}
		}

	}
}
