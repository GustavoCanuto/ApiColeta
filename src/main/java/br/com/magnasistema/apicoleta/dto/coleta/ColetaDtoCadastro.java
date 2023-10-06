package br.com.magnasistema.apicoleta.dto.coleta;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

public record ColetaDtoCadastro(
		
		@Past(message = "O timestamp não pode ser no futuro")
		@NotNull 
		LocalDateTime timestampInicial,

		@Past(message = "O timestamp não pode ser no futuro")
		LocalDateTime timestampFinal,

		@Positive(message = "O peso deve ser um valor positivo") 
		Double peso,

		@NotNull 
		Long idEquipe,

		@NotNull 
		Long idBairro,

		@NotNull 
		Long idDestino) {

	

		

}
