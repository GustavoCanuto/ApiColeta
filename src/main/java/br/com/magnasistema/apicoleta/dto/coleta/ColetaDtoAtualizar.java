package br.com.magnasistema.apicoleta.dto.coleta;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

public record ColetaDtoAtualizar(

		@NotNull
		@Past
		LocalDateTime timestampFinal,

		@NotNull
		@Positive(message = "O peso deve ser um valor positivo")
		Double peso
		
		) {

	

}
