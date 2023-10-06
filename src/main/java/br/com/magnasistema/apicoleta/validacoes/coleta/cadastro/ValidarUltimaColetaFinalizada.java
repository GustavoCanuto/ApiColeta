package br.com.magnasistema.apicoleta.validacoes.coleta.cadastro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoCadastro;
import br.com.magnasistema.apicoleta.entity.Coleta;
import br.com.magnasistema.apicoleta.repository.ColetaRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Component
public class ValidarUltimaColetaFinalizada implements ValidadorCadastroColeta {

	@Autowired
	private ColetaRepository coletaRepository;

	public void validar(ColetaDtoCadastro dados) {

		Coleta ultimaColetaDaEquipe = coletaRepository.findTopByEquipeIdOrderByIdDesc(dados.idEquipe());

		if (ultimaColetaDaEquipe != null && ultimaColetaDaEquipe.getTimestampFinal() == null) {
			
			throw new ValidacaoException("A equipe precisa finalizar a ultima coleta para registrar uma nova");
		}

	}

}
