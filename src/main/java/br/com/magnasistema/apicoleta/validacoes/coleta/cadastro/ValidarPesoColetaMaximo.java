package br.com.magnasistema.apicoleta.validacoes.coleta.cadastro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoCadastro;
import br.com.magnasistema.apicoleta.repository.EquipeRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Component
public class ValidarPesoColetaMaximo implements ValidadorCadastroColeta {

	@Autowired
	private EquipeRepository equipeRepository;

	public void validar(ColetaDtoCadastro dados) {

		var equipe = equipeRepository.getReferenceById(dados.idEquipe());

		if (dados.peso() != null && (dados.peso() > equipe.getVeiculo().getCapacidade())) {
			throw new ValidacaoException("O peso da coleta excede a capacidade do veículo da equipe.");

		}

	}

}
