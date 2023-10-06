package br.com.magnasistema.apicoleta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.magnasistema.apicoleta.dto.cidade.CidadeDtoDetalhar;
import br.com.magnasistema.apicoleta.repository.CidadeRepository;

@Service
public class CidadeService {

	@Autowired
	private CidadeRepository repository;

	public Page<CidadeDtoDetalhar> listarCidades(Pageable paginacao, String nome) {

		if (nome != null && !nome.isBlank()) { 
			
			return repository.findByNomeContainingIgnoreCase(nome, paginacao).map(CidadeDtoDetalhar::new);

		}
		return repository.findAll(paginacao).map(CidadeDtoDetalhar::new);

	}

	public Page<CidadeDtoDetalhar> listarCidadesPorEstado(Long id, String nome, Pageable paginacao) {

		if (nome != null && !nome.isBlank()) {
			
			return repository.findByEstadoIdAndNomeContainingIgnoreCase(id, nome, paginacao)
					.map(CidadeDtoDetalhar::new);

		}
		return repository.findByEstadoId(id, paginacao).map(CidadeDtoDetalhar::new);

	}

	public CidadeDtoDetalhar detalharCidade(Long id) {

		return new CidadeDtoDetalhar(repository.getReferenceById(id));

	}

}
