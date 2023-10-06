package br.com.magnasistema.apicoleta.validacoes.coleta.atualizar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoAtualizar;
import br.com.magnasistema.apicoleta.repository.ColetaRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Component
public class ValidarColetaFinalizada implements ValidadorAtualizacaoColeta {
	
	@Autowired
	private ColetaRepository coletaRepository;
	
	 public void validar(ColetaDtoAtualizar dados, Long id) {
		 
		 var coleta = coletaRepository.getReferenceById(id);

			if (coleta.getTimestampFinal() != null) {
				throw new ValidacaoException("A coleta já foi finalizada e não pode ser atualizada.");
			}
		 
	 }

}
