package br.com.magnasistema.apicoleta.dto.destino;

import br.com.magnasistema.apicoleta.dto.endereco.EnderecoDtoAtualizar;
import br.com.magnasistema.apicoleta.enums.TipoDestino;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

public record DestinoDtoAtualizar(

		String nome,

		TipoDestino tipoLocal,

		@Positive(message = "A capacidade suportada deve ser um número positivo.")
		Double capacidadeSuportada,
		
		@Valid
		EnderecoDtoAtualizar endereco,

		Long idEmpresa) {


}
