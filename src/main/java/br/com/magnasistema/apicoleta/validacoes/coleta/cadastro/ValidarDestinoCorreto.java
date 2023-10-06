package br.com.magnasistema.apicoleta.validacoes.coleta.cadastro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoCadastro;
import br.com.magnasistema.apicoleta.enums.TipoDestino;
import br.com.magnasistema.apicoleta.enums.TipoVeiculo;
import br.com.magnasistema.apicoleta.repository.DestinoRepository;
import br.com.magnasistema.apicoleta.repository.EquipeRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Component
public class ValidarDestinoCorreto implements ValidadorCadastroColeta {

	@Autowired
	private EquipeRepository equipeRepository;

	@Autowired
	private DestinoRepository destinoRepository;

	public void validar(ColetaDtoCadastro dados) {

		var equipe = equipeRepository.getReferenceById(dados.idEquipe());

		var destino = destinoRepository.getReferenceById(dados.idDestino());

		if (TipoVeiculo.CAMINHAO_DE_RECICLAGEM.equals(equipe.getVeiculo().getTipo())
				&& (!TipoDestino.RECICLAGEM.equals(destino.getTipoLocal()))) {
			
			throw new ValidacaoException(
					"Se o veículo da equipe é do tipo reciclagem, o destino também deve ser do tipo reciclagem.");

		}

	}

}
