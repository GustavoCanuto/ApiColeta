package br.com.magnasistema.apicoleta.dto.coleta;

import jakarta.validation.constraints.Positive;

public record ColetaDtoModificar(

		

		
		@Positive(message = "O peso deve ser um valor positivo")
		Double peso,
		
		Long idDestino
		
		) {

	

}
