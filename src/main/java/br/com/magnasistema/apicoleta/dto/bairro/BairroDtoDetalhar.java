package br.com.magnasistema.apicoleta.dto.bairro;

import br.com.magnasistema.apicoleta.entity.Bairro;
import br.com.magnasistema.apicoleta.entity.Cidade;

public record BairroDtoDetalhar(Long id,String nome, String logradouros, Cidade cidade) {
	
	public BairroDtoDetalhar(Bairro bairro) {
		this(bairro.getId(), bairro.getNome(), bairro.getLogradouros(), bairro.getCidade());
	}

}
