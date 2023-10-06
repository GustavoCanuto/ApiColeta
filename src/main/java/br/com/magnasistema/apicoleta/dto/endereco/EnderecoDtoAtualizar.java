package br.com.magnasistema.apicoleta.dto.endereco;

import jakarta.validation.constraints.Pattern;

public record EnderecoDtoAtualizar(

		 Long idCidade,

		 String logradouro,

		 String numero,

		 @Pattern(regexp = "\\d{8}",  message = "O Cep deve ter 8 numeros.") 
		 String cep) {

}
