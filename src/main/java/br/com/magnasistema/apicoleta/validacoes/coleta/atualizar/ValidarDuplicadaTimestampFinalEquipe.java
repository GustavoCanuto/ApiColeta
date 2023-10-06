package br.com.magnasistema.apicoleta.validacoes.coleta.atualizar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoAtualizar;
import br.com.magnasistema.apicoleta.repository.ColetaRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Component
public class ValidarDuplicadaTimestampFinalEquipe implements ValidadorAtualizacaoColeta {

	@Autowired
	private ColetaRepository coletaRepository;

	public void validar(ColetaDtoAtualizar dados, Long id) {
		
		 var coleta = coletaRepository.getReferenceById(id);

		boolean timestampDuplicado = coletaRepository.existsByEquipeIdAndTimestampInicialOrTimestampFinal(
				coleta.getEquipe().getId(), dados.timestampFinal(), dados.timestampFinal()); 

		if (timestampDuplicado) {
			throw new ValidacaoException("A equipe já tem uma coleta nesse timestamp");
		}
	}
}
