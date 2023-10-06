package br.com.magnasistema.apicoleta.dto.destino;

import br.com.magnasistema.apicoleta.dto.endereco.EnderecoDtoCadastrar;
import br.com.magnasistema.apicoleta.enums.TipoDestino;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DestinoDtoCadastro(
		
		@NotBlank
		String nome,
		
		@NotNull
		TipoDestino tipoLocal,
		
		@NotNull
		@Positive(message = "A capacidade suportada deve ser um número positivo.")
		Double capacidadeSuportada,
		
		@NotNull 
		@Valid
		EnderecoDtoCadastrar endereco,
			
		@NotNull 
		Long idEmpresa
		
		
		) {

}
