package br.com.magnasistema.apicoleta.service.buscador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.magnasistema.apicoleta.entity.Bairro;
import br.com.magnasistema.apicoleta.repository.BairroRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Component
public class BuscarBairro implements BuscarEntidade<Bairro>{

	@Autowired
	BairroRepository bairroRepository;
	
	Bairro bairro;
	
	public Bairro buscar(Long id) {

		if (id != null) {

			bairro = bairroRepository.findById(id)
					.orElseThrow(() -> new ValidacaoException("Id do Bairro não encontrado"));
		}

		return bairro;
	}


	
	
	
	
}
