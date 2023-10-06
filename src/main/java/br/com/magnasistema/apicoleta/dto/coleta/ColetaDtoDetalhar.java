package br.com.magnasistema.apicoleta.dto.coleta;

import java.time.LocalDateTime;

import br.com.magnasistema.apicoleta.entity.Bairro;
import br.com.magnasistema.apicoleta.entity.Coleta;
import br.com.magnasistema.apicoleta.entity.Destino;
import br.com.magnasistema.apicoleta.entity.Equipe;

public record ColetaDtoDetalhar (
		
		Long id,
		
		LocalDateTime timestampInicial,
		
		LocalDateTime timestampFinal,

		Double peso,
		
		Equipe equipe,

		Bairro bairro,

		Destino destino
		) 
		

{

	public ColetaDtoDetalhar(Coleta coleta) {
		this(coleta.getId(), coleta.getTimestampInicial(), coleta.getTimestampFinal(), coleta.getPeso(), coleta.getEquipe(), coleta.getBairro(), coleta.getDestino());

	}

}
