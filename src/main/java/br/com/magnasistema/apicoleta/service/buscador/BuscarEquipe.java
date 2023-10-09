package br.com.magnasistema.apicoleta.service.buscador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.magnasistema.apicoleta.entity.Equipe;
import br.com.magnasistema.apicoleta.repository.EquipeRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Component
public class BuscarEquipe implements BuscarEntidade<Equipe> {

	@Autowired
	EquipeRepository equipeRepository;

	Equipe equipe;

	public Equipe buscar(Long id) {

		equipe = equipeRepository.findById(id)
				.orElseThrow(() -> new ValidacaoException("Id da Equipe não encontrado"));

		return equipe;
	}

}
