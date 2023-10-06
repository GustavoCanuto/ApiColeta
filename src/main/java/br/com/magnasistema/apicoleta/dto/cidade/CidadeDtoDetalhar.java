package br.com.magnasistema.apicoleta.dto.cidade;

import br.com.magnasistema.apicoleta.dto.estado.EstadoDtoListagem;
import br.com.magnasistema.apicoleta.entity.Cidade;

public record CidadeDtoDetalhar(Long id,String nome, EstadoDtoListagem estado) {
	
	public CidadeDtoDetalhar(Cidade cidade) {
		this(cidade.getId(), cidade.getNome(), new EstadoDtoListagem(cidade.getEstado()));
	}
	

}
