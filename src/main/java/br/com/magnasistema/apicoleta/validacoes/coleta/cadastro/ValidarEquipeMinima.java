package br.com.magnasistema.apicoleta.validacoes.coleta.cadastro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoCadastro;
import br.com.magnasistema.apicoleta.entity.Funcionario;
import br.com.magnasistema.apicoleta.enums.TipoFuncao;
import br.com.magnasistema.apicoleta.repository.EquipeRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Component
public class ValidarEquipeMinima implements ValidadorCadastroColeta{

	@Autowired
	private EquipeRepository equipeRepository;

	public void validar(ColetaDtoCadastro dados) {
		
		var equipe = equipeRepository.getReferenceById(dados.idEquipe());
	
		boolean temColetor = false;
		boolean temMotorista = false;

		for (Funcionario f : equipe.getFuncionarios()) {

			if (TipoFuncao.COLETOR.equals(f.getFuncao())) {
				temColetor = true;
				continue;
			}

			if (TipoFuncao.MOTORISTA.equals(f.getFuncao())) {
				temMotorista = true;
			}
		}

		if (!temColetor || !temMotorista) {
			throw new ValidacaoException("A equipe precisa ter pelo menos 1 coletor e 1 motorista.");
		}
		
	}

}
