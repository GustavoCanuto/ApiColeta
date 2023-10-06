package br.com.magnasistema.apicoleta.dto.estado;

import br.com.magnasistema.apicoleta.entity.Estado;

public record EstadoDtoListagem(Long id,String nome,String uf) {

	public EstadoDtoListagem(Estado estado) {
		this(estado.getId(), estado.getNome(),estado.getUf());
	}
}
