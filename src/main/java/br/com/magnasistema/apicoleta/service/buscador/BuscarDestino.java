package br.com.magnasistema.apicoleta.service.buscador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.magnasistema.apicoleta.entity.Destino;
import br.com.magnasistema.apicoleta.repository.DestinoRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Component
public class BuscarDestino implements BuscarEntidade<Destino>{

	@Autowired
	DestinoRepository destinoRepository;
	
	Destino destino;
	
	public Destino buscar(Long id) {

		if (id != null) {

			destino = destinoRepository.findById(id)
					.orElseThrow(() -> new ValidacaoException("Id do Destino não encontrado"));
		}

		return destino;
	}


	
	
	
	
}
