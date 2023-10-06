package br.com.magnasistema.apicoleta.validacoes.buscador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.magnasistema.apicoleta.entity.Empresa;
import br.com.magnasistema.apicoleta.repository.EmpresaRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Component
public class BuscarEmpresa{

	@Autowired
	EmpresaRepository empresaRepository;
	
	Empresa empresa;
	
	public Empresa buscar(Long id) {

		if (id != null) {

			empresa = empresaRepository.findById(id)
					.orElseThrow(() -> new ValidacaoException("Id da cidade informada não existe!"));
		}

		return empresa;
	}


	
	
	
	
}
