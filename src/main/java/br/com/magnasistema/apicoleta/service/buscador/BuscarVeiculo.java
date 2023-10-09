package br.com.magnasistema.apicoleta.service.buscador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.magnasistema.apicoleta.entity.Veiculo;
import br.com.magnasistema.apicoleta.repository.VeiculoRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Component
public class BuscarVeiculo implements BuscarEntidade<Veiculo>{

	@Autowired
	VeiculoRepository veiculoRepository;
	
	Veiculo veiculo;
	
	public Veiculo buscar(Long id) {

		if (id != null) {

			veiculo = veiculoRepository.findById(id)
					.orElseThrow(() -> new ValidacaoException("Id do veiculo informada não existe!"));
		}

		return veiculo;
	}


	
	
	
	
}
