package br.com.magnasistema.apicoleta.validacoes.coleta.atualizar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoAtualizar;
import br.com.magnasistema.apicoleta.repository.ColetaRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Component
public class ValidarTimestamp implements ValidadorAtualizacaoColeta {

	@Autowired
	private ColetaRepository coletaRepository;

	public void validar(ColetaDtoAtualizar dados, Long id) {

		var coleta = coletaRepository.getReferenceById(id);

		if (coleta.getTimestampInicial().isAfter(dados.timestampFinal())) {
			throw new ValidacaoException("O timestampFinal deve ser depois do timestampInicial.");
		}

	}
}
