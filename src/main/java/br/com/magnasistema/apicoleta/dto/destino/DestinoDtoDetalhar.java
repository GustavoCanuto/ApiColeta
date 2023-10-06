package br.com.magnasistema.apicoleta.dto.destino;

import br.com.magnasistema.apicoleta.entity.Destino;
import br.com.magnasistema.apicoleta.entity.Empresa;
import br.com.magnasistema.apicoleta.entity.Endereco;
import br.com.magnasistema.apicoleta.enums.TipoDestino;

public record DestinoDtoDetalhar(

		Long id,

		String nome,

		TipoDestino tipoLocal,

		Double capacidadeSuportada,

		Endereco endereco,

		Empresa empresa)

{

	public DestinoDtoDetalhar(Destino destino) {
		this(destino.getId(), destino.getNome(), destino.getTipoLocal(), destino.getCapacidadeSuportada(),
				destino.getEndereco(), destino.getEmpresa());
	}

}
