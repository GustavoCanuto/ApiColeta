package br.com.magnasistema.apicoleta.validacoes.coleta.cadastro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoCadastro;
import br.com.magnasistema.apicoleta.repository.ColetaRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Component
public class ValidarDuplicadaTimestampEquipe implements ValidadorCadastroColeta {

	@Autowired
	private ColetaRepository coletaRepository;
	
	String mensagemDeValidacao = "A equipe já tem uma coleta nesse timestamp";

	public void validar(ColetaDtoCadastro dados) {

		boolean timestampDuplicado = coletaRepository.existsByEquipeIdAndTimestampInicialOrTimestampFinal(
				dados.idEquipe(), dados.timestampInicial(), dados.timestampInicial());
		
		if (timestampDuplicado) {
			throw new ValidacaoException(mensagemDeValidacao);
		}

		if (dados.timestampFinal() != null) {
			timestampDuplicado = coletaRepository.existsByEquipeIdAndTimestampInicialOrTimestampFinal(dados.idEquipe(),
					dados.timestampFinal(), dados.timestampFinal());
			
			if (timestampDuplicado) {
				throw new ValidacaoException(mensagemDeValidacao);
			}
		}

		

	}

}
