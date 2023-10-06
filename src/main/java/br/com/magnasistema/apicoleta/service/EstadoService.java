package br.com.magnasistema.apicoleta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.magnasistema.apicoleta.dto.estado.EstadoDtoListagem;
import br.com.magnasistema.apicoleta.repository.EstadoRepository;

@Service
public class EstadoService {
	
	@Autowired
	private EstadoRepository repository;

	public List<EstadoDtoListagem> listarEstados() {

		return repository.findAll().stream().map(EstadoDtoListagem::new).toList();
		
	
	}
}
